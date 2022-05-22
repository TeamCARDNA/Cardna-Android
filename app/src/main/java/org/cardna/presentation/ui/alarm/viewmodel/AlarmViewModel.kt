package org.cardna.presentation.ui.alarm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.dao.DeletedCardYouDao
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.friend.RequestAcceptOrDenyFriendData
import org.cardna.domain.repository.AlarmRepository
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.FriendRepository
import org.cardna.presentation.base.BaseViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    private val alarmRepository: AlarmRepository,
    private val cardRepository: CardRepository,
    private val deletedCardYouDao: DeletedCardYouDao
) : BaseViewModel() {

    private val _foldStatus = MutableLiveData(true)
    val foldStatus: LiveData<Boolean> = _foldStatus

    //리스트
    private val _friendRequest =
        MutableLiveData<List<ResponseGetAlarmData.Data.Request.Requester?>>()
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

    private val _cardContent = MutableLiveData<String>()
    val cardContent: LiveData<String> = _cardContent


    fun geAllAlarm() {
        viewModelScope.launch {
            runCatching {
                alarmRepository.getAlarm()
            }.onSuccess {
                it.data.apply {
                    CardNaRepository.alarmExistCount = request.requester.size + alarm.size
                    _friendRequest.value = request.requester
                    _writeCardYou.value = alarm
                    _isFriendRequestEmpty.value = request.count == 0
                    _isWriteCardYouEmpty.value = alarm.isEmpty()
                    _isAllAlarmEmpty.value =
                        _isFriendRequestEmpty.value == true && _isWriteCardYouEmpty.value == true
                    Log.e(
                        "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡgeAllAlarmㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ",
                        "${request.requester.size}+${alarm.size}+${CardNaRepository.alarmExistCount}"
                    )
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun acceptOrDenyFriend(friendId: Int, isAccept: Boolean) {
        viewModelScope.launch {
            runCatching {
                friendRepository.postAcceptOrDenyFriend(
                    RequestAcceptOrDenyFriendData(
                        friendId,
                        isAccept
                    )
                )
            }.onSuccess {
                if (it.data.status == "stranger")
                    _isRequestDeny.value = true
                Timber.e(it.status.toString())
            }.onFailure {
                Timber.e("throwable : $it")
            }
        }
    }

    fun setFriendRequestUnfold(foldStatus: Boolean) {
        _foldStatus.value = foldStatus
    }

    fun getDeletedCardYouList(cardId: Int) {
        viewModelScope.launch {
            runCatching {
                deletedCardYouDao.getAllDeletedCardYou()
            }.onSuccess { it ->
                if (it?.map { it.cardId }?.contains(cardId) == true) viewEvent(DELETED_CARD)
                else viewEvent(EXISTED_CARD)

            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun setDeletedCard(cardState: String) {
        when (cardState) {
            DELETED_CARD -> setCardSateResult(DELETED_CARD)
            EXISTED_CARD -> setCardSateResult(EXISTED_CARD)
        }
    }

    private fun setCardSateResult(state: String) = viewEvent(state)

    companion object {
        const val DELETED_CARD = "DELETED_CARD"
        const val EXISTED_CARD = "EXISTED_CARD"
    }
}

