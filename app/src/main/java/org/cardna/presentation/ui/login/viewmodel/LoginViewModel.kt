package org.cardna.presentation.ui.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.domain.repository.AuthRepository
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _test = MutableLiveData<Boolean>()
    val test: LiveData<Boolean> = _test

    private val _token = MutableLiveData<OAuthToken>()
    val token: LiveData<OAuthToken> = _token

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

    // 네이버 소셜로그인 콜백
    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            _naverSocialUserToken = NaverIdLoginSDK.getAccessToken().toString()
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//            shortToast("errorCode:$errorCode, errorDesc:$errorDescription")
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    fun getKakaoLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getKakaoLogin()
            }.onSuccess {
                Timber.d("login success : ${it.data}")
            }.onFailure {
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

                if(it.data.type == "signin"){ // 1. 재로그인
                    _loginType = "signin"
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                    CardNaRepository.userToken = it.data.accessToken // 헤더 토큰 갈아 끼우기
                }
                else{ // it.data.type == "signup" 2. 회원가입
                    _loginType = "signup"
                    CardNaRepository.userSocial = it.data.social
                    CardNaRepository.userUuid = it.data.uuid
                }
            }.onFailure {
                Timber.e("error $it")  //
            }
        }
    }


    fun getNaverTokenIssuance(){ // 토큰 재발급(API) 네이버
        viewModelScope.launch {
            kotlin.runCatching {
                // API 메서드 body 안에 header 를 accessToken 이랑 refreshToken 추가하면 되므로 requestData 필요 없음
                authRepository.getTokenIssuance(CardNaRepository.naverUserToken, CardNaRepository.naverUserRefreshToken)
            }.onSuccess {
                Timber.d("재발급 성공 : ${it.message}")

                CardNaRepository.naverUserToken = it.data.accessToken
                CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                CardNaRepository.userToken = it.data.accessToken // 헤더 토큰 갈아 끼우기

                _issuanceMessage = it.message
            }.onFailure {
                Timber.d("재발급 실패 : ${it.message}")
                _issuanceMessage = it.message!!
            }
        }
    }

    fun postSignUp(singUpData: RequestSignUpData) { // 이름 등록 및 회원가입 API
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {
                Timber.d("회원가입 성공 : ${it.message}")
                if(singUpData.social == "naver"){
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                }
                else{
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                }
            }.onFailure {
                Timber.d("회원가입 실패 : ${it.message}")
            }
        }
    }

    fun setToken(token: OAuthToken) {
        _token.value = token
    }
}