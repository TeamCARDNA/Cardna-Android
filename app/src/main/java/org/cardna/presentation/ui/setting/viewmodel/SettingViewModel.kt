package org.cardna.presentation.ui.setting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.domain.repository.UserRepository
import org.cardna.presentation.ui.setting.view.SecessionActivity
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _secessionReasonOneCheck = MutableLiveData(false)
    val secessionReasonOneCheck: LiveData<Boolean> = _secessionReasonOneCheck

    private val _secessionReasonTwoCheck = MutableLiveData(false)
    val secessionReasonTwoCheck: LiveData<Boolean> = _secessionReasonTwoCheck

    private val _secessionReasonThreeCheck = MutableLiveData(false)
    val secessionReasonThreeCheck: LiveData<Boolean> = _secessionReasonThreeCheck

    private val _secessionReasonFourCheck = MutableLiveData(false)
    val secessionReasonFourCheck: LiveData<Boolean> = _secessionReasonFourCheck

    private val _secessionReasonFiveCheck = MutableLiveData(false)
    val secessionReasonFiveCheck: LiveData<Boolean> = _secessionReasonFiveCheck

    private val _secessionReasonSixCheck = MutableLiveData(false)
    val secessionReasonSixCheck: LiveData<Boolean> = _secessionReasonSixCheck

    private val _isSecessionReasonValid = MutableLiveData(false)
    val isSecessionReasonValid: LiveData<Boolean> = _isSecessionReasonValid

    private val _secessionReasonList = MutableLiveData(mutableListOf<Int>())
    val secessionReasonList: LiveData<MutableList<Int>> = _secessionReasonList

    private val _etcContent = MutableLiveData<String?>()
    val etcContent: LiveData<String?> = _etcContent

    fun setSecessionReasonOneStatus(status: Boolean) {
        _secessionReasonOneCheck.value = status
        setSecessionReasonValid()
    }

    fun setSecessionReasonTwoStatus(status: Boolean) {
        _secessionReasonTwoCheck.value = status
        setSecessionReasonValid()
    }

    fun setSecessionReasonThreeStatus(status: Boolean) {
        _secessionReasonThreeCheck.value = status
        setSecessionReasonValid()
    }

    fun setSecessionReasonFourStatus(status: Boolean) {
        _secessionReasonFourCheck.value = status
        setSecessionReasonValid()
    }

    fun setSecessionReasonFiveStatus(status: Boolean) {
        _secessionReasonFiveCheck.value = status
        setSecessionReasonValid()
    }

    fun setSecessionReasonSixStatus(status: Boolean) {
        _secessionReasonSixCheck.value = status
        setSecessionReasonValid()
    }

    private fun setSecessionReasonValid() {
        _isSecessionReasonValid.value =
            _secessionReasonOneCheck.value == true || _secessionReasonTwoCheck.value == true || _secessionReasonThreeCheck.value == true ||
                    _secessionReasonFourCheck.value == true || _secessionReasonFiveCheck.value == true || _secessionReasonSixCheck.value == true
    }

    fun setEtcContent(etcContent: String) {
        _etcContent.value = etcContent
    }

    fun deleteUser() {
        if (_secessionReasonOneCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.ONE)
        }
        if (_secessionReasonTwoCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.TWO)
        }
        if (_secessionReasonThreeCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.THREE)
        }
        if (_secessionReasonFourCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.FOUR)
        }
        if (_secessionReasonFiveCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.FIVE)
        }
        if (_secessionReasonSixCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SIX)
        }

        viewModelScope.launch {
            runCatching {
                //TODO 소셜로그인 연동시 처리
                // userRepository.deleteUser( RequestDeleteUserData(_secessionReasonList.value!!, _etcContent?.value ?: ""))
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}