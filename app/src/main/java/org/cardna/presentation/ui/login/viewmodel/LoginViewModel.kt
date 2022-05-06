package org.cardna.presentation.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _test = MutableLiveData<Boolean>()
    val test: LiveData<Boolean> = _test

    private val _token = MutableLiveData<OAuthToken>()
    val token: LiveData<OAuthToken> = _token

    private val _userData = MutableLiveData<ResponseSocialLoginData.Data>()
    val userData: LiveData<ResponseSocialLoginData.Data> = _userData

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _issuanceTokenList = MutableLiveData<IssuanceTokenList>()
    val issuanceTokenList: LiveData<IssuanceTokenList> = _issuanceTokenList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getKakaoLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getKakaoLogin()
            }.onSuccess {
                _userData.value = it.data
                _isLogin.value = true
                Timber.d("login success : ${it.data}")
            }.onFailure {
                _isLogin.value = false
                Timber.e("error $it")
            }
        }
    }

    fun postSignUp(singUpData: RequestSignUpData) {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun getTokenIssuance() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getTokenIssuance()
            }.onSuccess {
                _message.value = it.message
                _issuanceTokenList.value =
                    IssuanceTokenList(it.data.accessToken, it.data.refreshToken)
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun setToken(token: OAuthToken) {
        _token.value = token
    }
}