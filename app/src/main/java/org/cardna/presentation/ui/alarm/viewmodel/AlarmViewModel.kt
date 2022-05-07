package org.cardna.presentation.ui.alarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.friend.RequestAcceptOrDenyFriendData
import org.cardna.domain.repository.AlarmRepository
import org.cardna.domain.repository.FriendRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    //리스트
    private val _friendRequest = MutableLiveData<List<ResponseGetAlarmData.Data.Request.Requester?>>()
    val friendRequest: LiveData<List<ResponseGetAlarmData.Data.Request.Requester?>> = _friendRequest

    private val _writeCardYou = MutableLiveData<List<ResponseGetAlarmData.Data.Alarm?>>()
    val writeCardYou: LiveData<List<ResponseGetAlarmData.Data.Alarm?>> = _writeCardYou

    //엠티인지 처리
    private val _isFriendRequestEmpty = MutableLiveData(true)
    val isFriendRequestEmpty: LiveData<Boolean> = _isFriendRequestEmpty

    private val _isWriteCardYouEmpty = MutableLiveData(true)
    val isWriteCardYouEmpty: LiveData<Boolean> = _isWriteCardYouEmpty

    private val _isAllAlarmEmpty = MutableLiveData(true)
    val isAllAlarmEmpty: LiveData<Boolean> = _isAllAlarmEmpty

    //거절 성공시 삭제되어야함
    private val _isRequestDeny = MutableLiveData<Boolean>()
    val isRequestDeny: LiveData<Boolean> = _isRequestDeny

    fun geAllAlarm() {
        viewModelScope.launch {
            runCatching {
                alarmRepository.getAlarm()
            }.onSuccess {
                it.data.apply {
                    _friendRequest.value = request.requester
                    _writeCardYou.value = alarm
                    _isFriendRequestEmpty.value = request.count == 0
                    _isWriteCardYouEmpty.value = alarm.isEmpty()
                    _isAllAlarmEmpty.value = _isFriendRequestEmpty.value == true && _isWriteCardYouEmpty.value == true
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun acceptOrDenyFriend(friendId: Int, isAccept: Boolean) {
        viewModelScope.launch {
            runCatching {
                friendRepository.postAcceptOrDenyFriend(RequestAcceptOrDenyFriendData(friendId, isAccept))
            }.onSuccess {
                if (it.data.status == "stranger") _isRequestDeny.value = true
                Timber.e(it.status.toString())
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}

