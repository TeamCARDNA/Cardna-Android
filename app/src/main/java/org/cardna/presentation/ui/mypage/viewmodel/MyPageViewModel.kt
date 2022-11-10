package org.cardna.presentation.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.friend.RequestApplyOrCancleFriendData
import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.domain.repository.FriendRepository
import org.cardna.domain.repository.MyPageRepository
import org.cardna.presentation.base.BaseViewModel
import org.cardna.presentation.ui.mypage.view.SearchFriendCodeActivity
import org.cardna.presentation.util.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val friendRepository: FriendRepository
) : BaseViewModel() {

    private val _settingBtnIsValid = MutableLiveData<Boolean>(false)
    val settingBtnIsValid: LiveData<Boolean> = _settingBtnIsValid

    private val _refreshFriendList = SingleLiveEvent<Any>()
    val refreshFriendList: LiveData<Any> = _refreshFriendList

    private val _myPage = MutableLiveData<ResponseMyPageData.Data>()
    val myPage: LiveData<ResponseMyPageData.Data> = _myPage

    private val _friendCount = MutableLiveData<String>()
    val friendCount: LiveData<String> = _friendCount

    private val _searchNameQuery = MutableLiveData<String>()
    val searchNameQuery: LiveData<String> = _searchNameQuery

    private val _searchCodeQuery = MutableLiveData<String>()
    val searchCodeQuery: LiveData<String> = _searchCodeQuery

    private val _searchFriendNameResult =
        MutableLiveData<List<ResponseMyPageData.Data.FriendList>>()
    val searchFriendNameResult: LiveData<List<ResponseMyPageData.Data.FriendList>> =
        _searchFriendNameResult
    private val _friendList = MutableLiveData<List<ResponseMyPageData.Data.FriendList>>()
    val friendList: LiveData<List<ResponseMyPageData.Data.FriendList>> = _friendList

    private val _isNonExistFriendName = MutableLiveData<Boolean>()
    val isNonExistFriend: LiveData<Boolean> = _isNonExistFriendName

    private val _searchFriendCodeResult = MutableLiveData<ResponseSearchFriendCodeData.Data>()
    val searchFriendCodeResult: LiveData<ResponseSearchFriendCodeData.Data> =
        _searchFriendCodeResult

    private val _isNonExistFriendCode = MutableLiveData<Boolean?>(null)
    val isNonExistFriendCode: LiveData<Boolean?> = _isNonExistFriendCode

    private val _friendRelationType = MutableLiveData<Int>()
    val friendRelationType: LiveData<Int> = _friendRelationType

    private val _friendId = MutableLiveData<Int>()
    val friendId: LiveData<Int> = _friendId

    private val _isInit = MutableLiveData<Boolean>(true)
    val isInit: LiveData<Boolean> = _isInit

    fun updateSearchNameQuery(name: String) {
        _searchNameQuery.value = name
        searchNamePost()
    }

    fun setQueryState(queryState: String) {
        when (queryState) {
            DEFAULT_STATE -> setSearchFriendNameResult(DEFAULT_STATE)
            SEARCH_QUERY -> setSearchFriendNameResult(SEARCH_QUERY)
            EXIST_QUERY -> setSearchFriendNameResult(EXIST_QUERY)
        }
    }

    private fun setSearchFriendNameResult(state: String) = viewEvent(state)

    fun searchNamePost() {
        val query = if (_searchNameQuery.value.isNullOrEmpty()) return else _searchNameQuery.value
        viewModelScope.launch {
            runCatching {
                friendRepository.getSearchFriendName(query!!)
            }.onSuccess {
                isNonExistFriendName(false)
                _searchFriendNameResult.value = it
                setQueryState(SEARCH_QUERY)
            }.onFailure {
                isNonExistFriendName(true)
                Timber.e(it.toString())
            }
        }
    }

    fun isNonExistFriendName(exist: Boolean) {
        _isNonExistFriendName.value = exist
    }

    fun updateSearchCodeQuery(code: String) {
        _searchCodeQuery.value = code
        Amplitude.getInstance().logEvent("My_SearchFriend_Code")
    }

    fun getUserMyPage() {
        viewModelScope.launch {
            runCatching {
                myPageRepository.getMyPage().data
            }.onSuccess {
                it.apply {
                    _myPage.value = it
                    _friendCount.value = friendList.size.toString()
                    _friendList.value = it.friendList  //제일처음 친구 리스트
                    if (_isInit.value == true) {
                        setQueryState(DEFAULT_STATE)
                        _isInit.value = false
                    }
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun setSearchCodeDefault(){
        _isNonExistFriendCode.value=null
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

    fun settingBtnIsValid(valid: Boolean) {
        _settingBtnIsValid.value = valid
    }

    fun refreshFriendList() {
        _refreshFriendList.call()
    }

    fun breakUpFriend() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
        applyOrCancelFriend()
    }

    fun cancelFriendRequest() {
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_ONE
        applyOrCancelFriend()
    }

    fun applyFriend() {
        Amplitude.getInstance().logEvent("My_SearchFriend_AddFriend")
        _friendRelationType.value = SearchFriendCodeActivity.RELATION_THREE
        applyOrCancelFriend()
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val EXIST_QUERY = "EXIST_QUERY"
        const val DEFAULT_STATE = "DEFAULT_STATE"
    }
}