package org.cardna.presentation.ui.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData
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

    //   val searchFriendName = MutableLiveData<MutableList<ResponseMyPageData.Data.FriendList>>()

    private val _isNonExistFriendName = MutableLiveData<Boolean>(false)
    val isNonExistFriend: LiveData<Boolean> = _isNonExistFriendName


    private val _searchFriendCodeResult = MutableLiveData<ResponseSearchFriendCodeData.Data>()
    val searchFriendCodeResult: LiveData<ResponseSearchFriendCodeData.Data> = _searchFriendCodeResult

    private val _friendRelationType = MutableLiveData<Int>()
    val friendRelationType: LiveData<Int> = _friendRelationType

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

    fun updateSearchNameQuery(query: String) {
        _searchNameQuery.value = query
    }

    fun updateSearchCodeQuery(query: String) {
        _searchCodeQuery.value = query
    }

    fun searchNamePost() {

        val query = _searchNameQuery.value ?: return
        viewModelScope.launch {
            runCatching {
                friendRepository.getSearchFriendName(query).data
            }.onSuccess {
                it.apply {
                    _isNonExistFriendName.value = false
                    _searchFriendNameResult.value = map { data ->
                        ResponseMyPageData.Data.FriendList(
                            data.id,
                            data.name,
                            data.userImg,
                            data.sentence ?: ""
                        )
                    }
                }
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
                    _friendRelationType.value = relationType
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    //모르는1->요청3
    fun applyFriend() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_THREE
    }

    //친구2->친구끊기1
    fun breakUpFriend() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
    }

    //요청3->요청취소1
    fun cancelFriendRequest() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
    }
}