package org.cardna.presentation.ui.alarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
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
}

