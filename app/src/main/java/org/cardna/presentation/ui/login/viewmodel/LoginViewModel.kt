package org.cardna.presentation.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.auth.IssuanceTokenList
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.domain.repository.AuthRepository
import org.cardna.domain.repository.CardRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

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

    private var _issuanceMessage = ""
    val issuanceMessage: String?
        get() = _issuanceMessage

    private var _tokenStatusCode = MutableLiveData<Int>()
    val tokenStatusCode: LiveData<Int> = _tokenStatusCode


    fun setTotalCardCnt() {
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

    private var _naverSocialUserToken = ""
    val naverSocialUserToken: String?
        get() = _naverSocialUserToken

    fun setNaverSocialUserToken(newNaverSocialUserToken: String) {
        _naverSocialUserToken = newNaverSocialUserToken
    }

    fun getKakaoLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getKakaoLogin()
            }.onSuccess {
                with(CardNaRepository) {
                    kakaoAccessToken = ""
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
            }.onFailure {
                _isLogin.value = false
                CardNaRepository.userSocial = KAKAO
                Timber.e("error $it")
            }
        }
    }

    fun getNaverLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getNaverLogin()
            }.onSuccess {
                with(CardNaRepository) {
                    naverAccessToken = ""
                    if (it.message == LOGIN_SUCCESS) { // 1. 재로그인
                        naverUserToken = it.data.accessToken
                        naverUserRefreshToken = it.data.refreshToken
                        userToken = it.data.accessToken
                        userSocial = NAVER
                        naverUserfirstName = it.data.name
                        naverUserlogOut = false
                        _gotoSetName.value = false
                    } else {
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
                    kakaoUserToken = it.data.accessToken
                    kakaoUserRefreshToken = it.data.refreshToken
                    userToken = kakaoUserToken
                    _tokenStatusCode.value = it.status
                }
            }.onFailure {
                when (it) {
                    is retrofit2.HttpException -> {
                        _tokenStatusCode.value = it.code()
                    }
                }
            }
        }
    }

    fun getNaverTokenIssuance() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getTokenIssuance(
                    CardNaRepository.naverUserToken,
                    CardNaRepository.naverUserRefreshToken
                )
            }.onSuccess {
                CardNaRepository.naverUserToken = it.data.accessToken
                CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                CardNaRepository.userToken = it.data.accessToken
                _tokenStatusCode.value = it.status
            }.onFailure {
                when (it) {
                    is retrofit2.HttpException -> {
                        _tokenStatusCode.value = it.code()
                    }
                }
            }
        }
    }

    fun postSignUp(singUpData: RequestSignUpData) {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {
                _isLogin.value = true
                if (singUpData.social == "naver") {
                    CardNaRepository.naverUserToken = it.data.accessToken
                    CardNaRepository.naverUserRefreshToken = it.data.refreshToken
                    CardNaRepository.naverUserfirstName = it.data.name
                    CardNaRepository.naverUserlogOut = false
                    CardNaRepository.userToken = it.data.accessToken
                } else {
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