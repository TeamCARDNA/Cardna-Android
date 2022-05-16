package org.cardna.presentation.ui.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.auth.IssuanceTokenList
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData
import org.cardna.domain.repository.AuthRepository
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _tokenList = MutableLiveData<IssuanceTokenList>()
    val tokenList: LiveData<IssuanceTokenList> = _tokenList

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> = _refreshToken

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _socialType = MutableLiveData<String>()
    val socialType: LiveData<String> = _socialType


    // 토큰 재발급 메서드에 대한 message
    private var _issuanceMessage = ""
    val issuanceMessage: String?
        get() = _issuanceMessage

    // 소셜로그인 API 호출 시, 1. 회원가입 인지 2. 재로그인 인지
    private var _loginType = "signin"
    val loginType: String
        get() = _loginType

    // 네이버 소셜 토큰
    private var _naverSocialUserToken = ""
    val naverSocialUserToken: String?
        get() = _naverSocialUserToken

    // 네이버 소셜 토큰 set 메서드
    fun setNaverSocialUserToken(newNaverSocialUserToken: String) {
        _naverSocialUserToken = newNaverSocialUserToken
    }

    fun getKakaoLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getKakaoLogin()
            }.onSuccess {
                with(CardNaRepository) {
                    //로그인 성공
                    kakaoAccessToken = ""
                    if (it.message == LOGIN_SUCCESS) {
                        kakaoUserToken = it.data.accessToken
                        kakaoUserRefreshToken = it.data.refreshToken
                        userSocial = KAKAO
                        _isLogin.value = true
                        kakaoUserlogOut = false
                    } else {
                        //탈퇴했거나 가입하지 않은 유저
                        userSocial = KAKAO
                        userUuid=it.data.uuid
                        _isLogin.value = false  
                    }
                }
                Timber.d("login success : ${it.data}")
            }.onFailure {
                //비회원 or 토큰이 올바르지 않은 경우
                _isLogin.value = false
                CardNaRepository.userSocial = KAKAO
                Timber.e("error $it")
            }
        }
    }

    fun getNaverLogin() { // 소셜로그인 API => 1. 토큰 만료되어 재로그인 or 2. 신규유저 회원가입
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getNaverLogin(CardNaRepository.fireBaseToken)
            }.onSuccess {
                Timber.d("login success : ${it.data}")

                if (it.data.type == "signin") { // 1. 재로그인
                    _loginType = "signin"
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                    CardNaRepository.userToken = it.data.accessToken // 헤더 토큰 갈아 끼우기
                } else { // it.data.type == "signup" 2. 회원가입
                    _loginType = "signup"
                    // 아직 이름등록 및 회원가입 전인데 social이랑 uuid를 shardPre에 저장해둬도 되나
                    CardNaRepository.userSocial = it.data.social
                    CardNaRepository.userUuid = it.data.uuid
                }
            }.onFailure {
                Timber.e("error $it")  //
            }
        }
    }


    fun getNaverTokenIssuance() { // 토큰 재발급(API) 네이버
        viewModelScope.launch {
            kotlin.runCatching {
                // API 메서드 body 안에 header 를 accessToken 이랑 refreshToken 추가하면 되므로 requestData 필요 없음
                authRepository.getTokenIssuance(
                    CardNaRepository.naverUserToken,
                    CardNaRepository.naverUserRefreshToken
                )
            }.onSuccess {
                CardNaRepository.naverUserToken = it.data.accessToken
                CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                CardNaRepository.userToken = it.data.accessToken // 헤더 토큰 갈아 끼우기

                _issuanceMessage = it.message
            }.onFailure {
                Timber.d("재발급 실패 : ${it.message}")
                _isLogin.value = false
            }
        }
    }

    fun postSignUp(singUpData: RequestSignUpData) { // 이름 등록 및 회원가입 API
        Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ회원가입  : postSignUp")
        Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ회원가입 성공 : ${singUpData}")
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {
                Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ회원가입 성공 : ${it.message}")
                _isLogin.value = true
                if (singUpData.social == "naver") {
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                } else { // kakao
                    CardNaRepository.kakaoUserToken = it.data.accessToken
                    CardNaRepository.kakaoUserRefreshToken = it.data.refreshToken
                    CardNaRepository.kakaoUserfirstName=singUpData.firstName
                    CardNaRepository.userToken=it.data.accessToken
                    Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ회원가입 성공 : ${it}")
                }
            }.onFailure {
                Timber.d("회원가입 실패 : ${it.message}")
                _isLogin.value = false
            }
        }
    }

    fun setAccessToken(token: String) {
        _accessToken.value = token
    }

    fun setRefreshToken(token: String) {
        _refreshToken.value = token
    }

    companion object {
        const val LOGIN_SUCCESS = "로그인 성공"
        const val KAKAO = "kakao"
        const val NAVER = "naver"
    }
}