package org.cardna.presentation.ui.login.viewmodel

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

    fun getKakaoLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getKakaoLogin()
            }.onSuccess {
                with(CardNaRepository) {
                    //로그인 성공
                    if (it.message == LOGIN_SUCCESS) {
                        kakaoUserToken = it.data.accessToken
                        kakaoUserRefreshToken = it.data.refreshToken
                        kakaoUserfirstName = it.data.name
                        userSocial = KAKAO
                        _isLogin.value = true
                        kakaoUserlogOut = false
                        Timber.d("accessToken : ${it.data.accessToken}")
                        Timber.d("refreshToken : ${it.data.refreshToken}")
                    } else {
                        //탈퇴했거나 가입하지 않은 유저
                        userSocial = KAKAO
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

    fun postSignUp(singUpData: RequestSignUpData) {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {
                with(CardNaRepository) {
                    kakaoUserfirstName = it.data.name
                    kakaoUserToken = it.data.accessToken
                    kakaoUserRefreshToken = it.data.refreshToken
                    kakaoUserlogOut = false
                    Timber.d("post name : $kakaoUserfirstName")
                }
                _isLogin.value = true
            }.onFailure {
                _isLogin.value = false
            }
        }
    }

    fun getTokenIssuance() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getTokenIssuance()
            }.onSuccess {
                _message.value = it.message
                CardNaRepository.kakaoUserToken = it.data.accessToken
                CardNaRepository.kakaoUserRefreshToken = it.data.refreshToken
                Timber.d("토큰 재발급 메서드")
            }.onFailure {
                _message.value = it.message
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