package org.cardna.presentation.ui.setting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.domain.repository.AlarmRepository
import org.cardna.domain.repository.UserRepository
import org.cardna.presentation.ui.setting.view.SecessionActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _pushAlarmOn = MutableLiveData(CardNaRepository.pushAlarmOn)
    val pushAlarmOn: LiveData<Boolean> = _pushAlarmOn

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

    private val _isEtcContentValid = MutableLiveData(false)
    val isEtcContentValid: LiveData<Boolean> = _isEtcContentValid

    private val _isSecessionReasonValid = MutableLiveData(false)
    val isSecessionReasonValid: LiveData<Boolean> = _isSecessionReasonValid

    private val _isDeleteUserSuccess = MutableLiveData<Boolean>()
    val isDeleteUserSuccess: LiveData<Boolean> = _isDeleteUserSuccess

    private val _secessionReasonList = MutableLiveData(mutableListOf<Int>())
    val secessionReasonList: LiveData<MutableList<Int>> = _secessionReasonList

    private val _etcContent = MutableLiveData<String?>()
    val etcContent: LiveData<String?> = _etcContent

    private val _isAcceptPush = MutableLiveData<Boolean>(true)
    val isAcceptPush: LiveData<Boolean> = _isAcceptPush

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

    fun setEtcContentStatus(status: Boolean) {
        _isEtcContentValid.value = status
        setSecessionReasonValid()
    }

    private fun setSecessionReasonValid() {
        _isSecessionReasonValid.value =
            _secessionReasonOneCheck.value == true || _secessionReasonTwoCheck.value == true || _secessionReasonThreeCheck.value == true ||
                    _secessionReasonFourCheck.value == true || _secessionReasonFiveCheck.value == true || (_secessionReasonSixCheck.value == true && _isEtcContentValid.value == true)

        if (_secessionReasonSixCheck.value == true && _isEtcContentValid.value == false)
            _isSecessionReasonValid.value = false
    }

    fun setEtcContent(etcContent: String, status: Boolean) {
        setEtcContentStatus(!status)
        _etcContent.value = etcContent
    }

    fun deleteUser() {
        if (_secessionReasonOneCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_ONE)
        }
        if (_secessionReasonTwoCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_TWO)
        }
        if (_secessionReasonThreeCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_THREE)
        }
        if (_secessionReasonFourCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_FOUR)
        }
        if (_secessionReasonFiveCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_FIVE)
        }
        if (_secessionReasonSixCheck.value == true) {
            _secessionReasonList.value?.add(SecessionActivity.SECESSION_REASON_SIX)
        }

        viewModelScope.launch {
            runCatching {
                userRepository.deleteUser(RequestDeleteUserData(_secessionReasonList.value!!, _etcContent.value ?: ""))
            }.onSuccess {
                CardNaRepository.apply {
                    if (userSocial == "kakao") {
                        userSocial = ""
                        userUuid = ""
                        kakaoUserfirstName = ""
                        kakaoUserToken = ""
                        kakaoUserRefreshToken = ""
                    } else {
                        userSocial = ""
                        userUuid = ""

                        naverUserfirstName = ""
                        naverUserToken = ""
                        naverUserRefreshToken = ""
                    }
                }
                _isDeleteUserSuccess.value = true
            }.onFailure {
                _isDeleteUserSuccess.value = false
                Timber.e(it.message)
            }
        }
    }

    fun switchPushAlarm() {
        CardNaRepository.pushAlarmOn = !CardNaRepository.pushAlarmOn
        Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "$CardNaRepository.pushAlarmOn")
        _pushAlarmOn.value = CardNaRepository.pushAlarmOn
    }
}