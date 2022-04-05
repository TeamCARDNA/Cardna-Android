package org.cardna.presentation.ui.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.friend.RequestApplyOrCancleFriendData
import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.domain.repository.FriendRepository
import org.cardna.domain.repository.MyPageRepository
import org.cardna.presentation.ui.mypage.view.SearchFriendCodeActivity
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _myPage = MutableLiveData<ResponseMyPageData.Data>()
    val myPage: LiveData<ResponseMyPageData.Data> = _myPage

    private val _friendCount = MutableLiveData<String>()
    val friendCount: LiveData<String> = _friendCount

    private val _searchNameQuery = MutableLiveData<String>()
    val searchNameQuery: LiveData<String> = _searchNameQuery

    private val _searchCodeQuery = MutableLiveData<String>()
    val searchCodeQuery: LiveData<String> = _searchCodeQuery

    private val _searchFriendNameResult = MutableLiveData<List<ResponseMyPageData.Data.FriendList>>()
    val searchFriendNameResult: LiveData<List<ResponseMyPageData.Data.FriendList>> = _searchFriendNameResult

    private val _isNonExistFriendName = MutableLiveData<Boolean>(false)
    val isNonExistFriend: LiveData<Boolean> = _isNonExistFriendName

    private val _searchFriendCodeResult = MutableLiveData<ResponseSearchFriendCodeData.Data>()
    val searchFriendCodeResult: LiveData<ResponseSearchFriendCodeData.Data> = _searchFriendCodeResult

    private val _isNonExistFriendCode = MutableLiveData<Boolean>(false)
    val isNonExistFriendCode: LiveData<Boolean> = _isNonExistFriendCode

    private val _friendRelationType = MutableLiveData<Int>()
    val friendRelationType: LiveData<Int> = _friendRelationType

    private val _friendId = MutableLiveData<Int>()
    val friendId: LiveData<Int> = _friendId

    private val _updateSearchNameQuerySuccess = MutableLiveData<Boolean>(true)
    val updateSearchNameQuerySuccess: LiveData<Boolean> = _updateSearchNameQuerySuccess

    fun updateSearchNameQuery(name: String) {
        _searchNameQuery.value = name
    }

    fun setUpdateSearchNameQueryState(state: Boolean) {
        _updateSearchNameQuerySuccess.value = state
    }

    fun updateSearchCodeQuery(code: String) {
        _searchCodeQuery.value = code
    }

    fun getUserMyPage() {
        viewModelScope.launch {
            runCatching {
                myPageRepository.getMyPage().data
            }.onSuccess {
                it.apply {
                    _myPage.value = it
                    _friendCount.value = friendList.size.toString()
                    _isNonExistFriendName.value = friendList.size == 0
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun searchNamePost() {
        val query = if (_searchNameQuery.value.isNullOrEmpty()) return else _searchNameQuery.value
        viewModelScope.launch {
            runCatching {
                friendRepository.getSearchFriendName(query!!)
            }.onSuccess {
                _isNonExistFriendName.value = false
                _searchFriendNameResult.value = it
            }.onFailure {
                _isNonExistFriendName.value = true
                Timber.e(it.toString())
            }
        }
    }

    fun searchCodePost() {
        val query = _searchCodeQuery.value ?: return
        viewModelScope.launch {
            runCatching {
                friendRepository.getSearchFriendCode(query).data
            }.onSuccess {
                it.apply {
                    _searchFriendCodeResult.value = it
                    _isNonExistFriendCode.value = false
                    _friendRelationType.value = relationType
                    _friendId.value = id
                }
            }.onFailure {
                _isNonExistFriendCode.value = true
                Timber.e(it.toString())
            }
        }
    }

    fun applyOrCancelFriend() {
        val friendId = _friendId.value ?: return
        viewModelScope.launch {
            runCatching {
                friendRepository.postApplyOrCancleFriend(RequestApplyOrCancleFriendData(friendId))
            }.onSuccess {
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    //친구2->손절1
    fun breakUpFriend() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
        applyOrCancelFriend()
    }

    //요청3->요청취소1
    fun cancelFriendRequest() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
        applyOrCancelFriend()
    }

    //몰라1->요청3
    fun applyFriend() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_THREE
        applyOrCancelFriend()
    }
}