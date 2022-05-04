package org.cardna.presentation.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.domain.repository.AuthRepository
import org.cardna.domain.repository.CardRepository
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

    fun getNaverLogin() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getNaverLogin()
            }.onSuccess {
                Timber.d("login success : ${it.data}")
            }.onFailure {
                Timber.e("error $it")
            }
        }
    }


    fun getNaverTokenIssuance() : Int{
        var status: Int = 0
        // header에 accesstoken이랑 refreshtoken 추가
        viewModelScope.launch {
            kotlin.runCatching {
                // header에 accesstoken이랑 refreshtoken 추가하면 되므로 requestData 필요 없음
                authRepository.getTokenIssuance(CardNaRepository.naverUserToken, CardNaRepository.naverUserRefreshToken)
            }.onSuccess {
                Timber.d("재발급 성공 : ${it.message}")
                status = it.status
            }.onFailure {
                Timber.d("재발급 실패 : ${it.message}")
            }
        }
        return status
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