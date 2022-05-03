package org.cardna.presentation.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.auth.RequestSignUpData
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

    fun postSignUp(singUpData: RequestSignUpData) {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(singUpData)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun setToken(token: OAuthToken) {
        _token.value = token
    }
}