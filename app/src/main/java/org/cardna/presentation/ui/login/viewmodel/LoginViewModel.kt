package org.cardna.presentation.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.api.auth.AuthService
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val _test = MutableLiveData<Boolean>()
    val test: LiveData<Boolean> = _test

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
}