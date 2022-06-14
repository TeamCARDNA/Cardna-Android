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
import org.cardna.presentation.ui.login.view.SetNameActivity.Companion.KAKAO
import org.cardna.presentation.ui.login.view.SetNameActivity.Companion.NAVER
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    // 그 사람의 카드팩의 총 카드 개수 => CardPackFragment 의 textView 에 바인딩
    private val _totalCardCnt = MutableLiveData<Int>()
    val totalCardCnt: LiveData<Int>
        get() = _totalCardCnt


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

    private val _gotoSetName = MutableLiveData<Boolean>()
    val gotoSetName: LiveData<Boolean> = _gotoSetName


    // 토큰 재발급 메서드에 대한 message
    private var _issuanceMessage = ""
    val issuanceMessage: String?
        get() = _issuanceMessage

    // 토큰 재발급 메서드에 대한 message
    private var _tokenStatusCode = MutableLiveData<Int>()
    val tokenStatusCode: LiveData<Int> = _tokenStatusCode


    fun setTotalCardCnt() { // 본인 카드팩 접근시에만 필요
        viewModelScope.launch {
            runCatching {
                cardRepository.getCardAll().data
            }.onSuccess {
                _totalCardCnt.value = it.totalCardCnt
                Timber.e("CardPack: setTotalCnt")
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

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
                    kakaoAccessToken = ""  //todo 인터셉트바꾸기 위함
                    if (it.message == LOGIN_SUCCESS) {
                        kakaoUserToken = it.data.accessToken
                        kakaoUserRefreshToken = it.data.refreshToken
                        userSocial = KAKAO
                        _isLogin.value = true
                        userToken = kakaoUserToken
                        kakaoUserfirstName = it.data.name
                        kakaoUserlogOut = false
                    } else {
                        //탈퇴했거나 가입하지 않은 유저
                        userSocial = KAKAO
                        userUuid = it.data.uuid
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
                authRepository.getNaverLogin()
            }.onSuccess {
                with(CardNaRepository) {
                    Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ네이버로그인ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ $it")
                    naverAccessToken = ""  //todo 인터셉트바꾸기 위함
                    if (it.message == LOGIN_SUCCESS) { // 1. 재로그인
                        Timber.e("네이버 로그인 재로그인")
                        naverUserToken = it.data.accessToken
                        naverUserRefreshToken = it.data.refreshToken
                        userToken = it.data.accessToken
                        userSocial = NAVER
                        naverUserfirstName = it.data.name
                        naverUserlogOut = false
                        _gotoSetName.value = false
                    } else { // 2. 회원가입
                        Timber.e("네이버 로그인 회원가입")
                        userSocial = it.data.social
                        userUuid = it.data.uuid
                        _gotoSetName.value = true
                    }
                }
            }.onFailure {
                Timber.e("error $it")
            }
        }
    }

    fun getKakaoTokenIssuance() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getTokenIssuance(
                    CardNaRepository.kakaoUserToken,
                    CardNaRepository.kakaoUserRefreshToken
                )
            }.onSuccess {
                with(CardNaRepository) {
                    Timber.d("KKK 재발급 상태 코드 : ${it.status}")
                    kakaoUserToken = it.data.accessToken
                    kakaoUserRefreshToken = it.data.refreshToken
                    userToken = kakaoUserToken
                    _tokenStatusCode.value = it.status
                }
            }.onFailure {
                when (it) {
                    is retrofit2.HttpException -> {
                        _tokenStatusCode.value = it.code()
                        Timber.d("KKK 재발급 상태 코드 : ${it.code()}")
                    }
                }
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
                Timber.d("재발급 메세지 : ${it.message}")
                Timber.d("새로운 accessToken : ${it.data.accessToken}")
                Timber.d("새로운 refreshToken : ${it.data.refreshToken}")
                CardNaRepository.naverUserToken = it.data.accessToken
                CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                CardNaRepository.userToken = it.data.accessToken // 헤더 토큰 갈아 끼우기
                _tokenStatusCode.value = it.status // 200 일것
            }.onFailure {
                when (it) {
                    is retrofit2.HttpException -> {
                        _tokenStatusCode.value = it.code()
                        Timber.d("onFailure 재발급 상태 코드 : ${it.code()}")
                    }
                }
            }
        }
    }

    fun postSignUp(singUpData: RequestSignUpData) { // 이름 등록 및 회원가입 API
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {
                _isLogin.value = true
                if (singUpData.social == "naver") {
                    Timber.e("네이버 로그인 이름 등록 및 회원가입 API 성공")
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                    CardNaRepository.naverUserfirstName = it.data.name
                    CardNaRepository.naverUserlogOut = false
                    CardNaRepository.userToken = it.data.accessToken
                } else { // kakao
                    CardNaRepository.kakaoUserToken = it.data.accessToken
                    CardNaRepository.kakaoUserRefreshToken = it.data.refreshToken
                    CardNaRepository.kakaoUserfirstName = singUpData.firstName
                    CardNaRepository.userToken = it.data.accessToken
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