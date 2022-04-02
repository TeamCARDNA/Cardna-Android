package org.cardna.presentation.ui.alarm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.friend.RequestAcceptOrDenyFriendData
import org.cardna.data.remote.model.friend.RequestApplyOrCancleFriendData
import org.cardna.domain.repository.FriendRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    //  private val _friendRequest = MutableLiveData<List<ResponseUserPostData.Data.Post?>>()
    //  val friendRequest: LiveData<List<ResponseUserPostData.Data.Post?>> = _friendRequest

    //  private val _writeCardYou = MutableLiveData<List<ResponseUserPostData.Data.Post?>>()
    //  val writeCardYou: LiveData<List<ResponseUserPostData.Data.Post?>> = _writeCardYou

    private val _isFriendRequestEmpty = MutableLiveData(false)
    val isFriendRequestEmpty: LiveData<Boolean> = _isFriendRequestEmpty

    private val _isWriteCardYouEmpty = MutableLiveData(false)
    val isWriteCardYouEmpty: LiveData<Boolean> = _isWriteCardYouEmpty

    fun getFriendRequest() {
        viewModelScope.launch {
            runCatching {

            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun getWriteCardYou() {
        viewModelScope.launch {
            runCatching {

            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun acceptOrDenyFriend(friendId: Int, isAccept: Boolean) {
        Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ",friendId.toString()+isAccept.toString())
        viewModelScope.launch {
            runCatching {
                friendRepository.postAcceptOrDenyFriend(RequestAcceptOrDenyFriendData(friendId, isAccept))
            }.onSuccess {
                Timber.e(it.status.toString())
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}

