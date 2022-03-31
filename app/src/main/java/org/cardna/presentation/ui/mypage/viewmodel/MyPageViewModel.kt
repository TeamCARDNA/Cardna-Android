package org.cardna.presentation.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.insight.ResponseInsightData
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.domain.repository.MyPageRepository
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {


    private val _myPage = MutableLiveData<ResponseMyPageData.Data>()
    val myPage: LiveData<ResponseMyPageData.Data> = _myPage

    private val _friendCount = MutableLiveData<String>()
    val friendCount: LiveData<String> = _friendCount

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun getMyPage() {
        viewModelScope.launch {
            runCatching {
                myPageRepository.getMyPage().data
            }.onSuccess {
                _myPage.value = it
                _friendCount.value = it.friendList.size.toString()

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
          /*  _searchPostAllData.value = repository.searchPost(query).data.post.map { data ->
                ResponsePostAllData.Data.Post(
                    data.id,
                    data.author,
                    data.title,
                    data.category,
                    data.content,
                    data.commentCount,
                    data.createdAt,
                    data.updatedAt
                )
            }*/
        }
    }
}