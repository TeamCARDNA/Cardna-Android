package org.cardna.presentation.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.friend.ResponseFriendNameData
import org.cardna.data.remote.model.insight.ResponseInsightData
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.domain.repository.FriendRepository
import org.cardna.domain.repository.MyPageRepository
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

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _searchFriendNameResult = MutableLiveData<ResponseFriendNameData.Data>()
    val searchFriendNameResult: LiveData<ResponseFriendNameData.Data> = _searchFriendNameResult

    val searchFriendName = MutableLiveData<MutableList<ResponseMyPageData.Data.FriendList>>()

    private val _isNonExistFriend = MutableLiveData<Boolean>()
    val isNonExistFriend: LiveData<Boolean> = _isNonExistFriend

    fun getUserMyPage() {
        viewModelScope.launch {
            runCatching {
                myPageRepository.getMyPage().data
            }.onSuccess {
                it.apply {
                    _myPage.value = it
                    _friendCount.value = friendList.size.toString()
                    _isNonExistFriend.value = friendList.size == 0
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }

    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchPost() {
        val query = _searchQuery.value ?: return
        viewModelScope.launch {
            runCatching {
                friendRepository.getFriendName(query).data
            }.onSuccess {
                it.apply {
                    _isNonExistFriend.value = false
                    searchFriendName.value?.add(
                        ResponseMyPageData.Data.FriendList(id, name, userImg, sentence)
                    )
                }
            }.onFailure {
                _isNonExistFriend.value = true
                Timber.e(it.toString())
            }
        }
    }
}