package org.cardna.presentation.ui.maincard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.MainCard
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.data.remote.model.friend.RequestApplyOrCancleFriendData
import org.cardna.data.remote.model.friend.ResponseApplyOrCancleFriendData
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.FriendRepository
import org.cardna.domain.repository.MyPageRepository
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainCardViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val myPageRepository: MyPageRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {

    private val _isMyCard = MutableLiveData<Boolean>()
    val isMyCard: LiveData<Boolean> = _isMyCard

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _cardList = MutableLiveData<List<MainCard>>()
    val cardList: LiveData<List<MainCard>> = _cardList

    private val _cardId = MutableLiveData<Int>()
    val cardId: LiveData<Int> = _cardId

    private val _isBlocked = MutableLiveData<Boolean>()
    val isBlocked: LiveData<Boolean> = _isBlocked

    private val _cardPosition = MutableLiveData(0)
    val cardPosition: LiveData<Int> = _cardPosition

    private val _relation = MutableLiveData<Any>()
    val relation: LiveData<Any> = _relation

    fun setRelation(friendRelation: String) {
        _relation.value = friendRelation
    }

    fun getMainCardList(id: Int? = -1) {
        viewModelScope.launch {
            runCatching {
                if (id == -1)
                    cardRepository.getMainCard().data
                else
                    id?.let { cardRepository.getOtherMainCard(it).data }
            }.onSuccess {
                if (it != null) {
                    _isMyCard.value = it.isMyCard
                    _cardList.value = it.mainCardList
                    _isBlocked.value = it.isBlocked
                    _relation.value = "${it.relation}"
                    it.mainCardList.map {
                        _cardId.value = it.id
                    }
                }
            }.onFailure {
                Timber.e("ViewModel connect fail")
            }
        }
    }

    fun getMyPageUser(otherUserName: String = "none") {
        viewModelScope.launch {
            kotlin.runCatching {
                myPageRepository.getMyPageUser().data
            }.onSuccess {
                //갑자기 strings 추가가 안되는데?
                if (otherUserName == "none")
                    _name.value = it.name
                else
                    _name.value = otherUserName
            }.onFailure {
                Timber.e("viewModel connect fail")
            }
        }
    }

    fun postFriendRequest(friendId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                friendRepository.postApplyOrCancleFriend(RequestApplyOrCancleFriendData(friendId))
            }.onSuccess {
                Timber.d("friendId : $friendId")
                Timber.d("message : ${it.message}")
            }.onFailure {
                Timber.e("message : ${it.message}")
            }
        }
    }

    fun saveInitCardPosition(cardPosition: Int) {
        _cardPosition.value = cardPosition
    }
}