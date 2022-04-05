package org.cardna.presentation.ui.maincard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.MyPageRepository
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainCardViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val myPageRepository: MyPageRepository
) : ViewModel() {

    private val _cardImg = MutableLiveData<String>()
    val cardImg: LiveData<String> = _cardImg

    private val _isMe = MutableLiveData<Boolean>()
    val isMe: LiveData<Boolean> = _isMe

    private val _mainOrder = MutableLiveData<Int>()
    val mainOrder: LiveData<Int> = _mainOrder

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _isMyCard = MutableLiveData<Boolean>()
    val isMyCard: LiveData<Boolean> = _isMyCard

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _cardList = MutableLiveData<List<ResponseMainCardData.Data.MainCard>>()
    val cardList: LiveData<List<ResponseMainCardData.Data.MainCard>> = _cardList

    private val _cardAllCount = MutableLiveData<Int>()
    val cardAllCount: LiveData<Int> = _cardAllCount

    fun getMainCardList() {
        viewModelScope.launch {
            runCatching {
                cardRepository.getMainCard().data
            }.onSuccess { it ->
                _isMyCard.value = it.isMyCard
                _cardList.value = it.mainCardList
                _cardAllCount.value = it.mainCardList.size
                it.mainCardList.map {
                    _cardImg.value = it.cardImg
                    _isMe.value = it.isMe
                    _mainOrder.value = it.mainOrder
                    _title.value = it.title
                }
            }.onFailure {
                Timber.e("ViewModel connect fail")
            }
        }
    }

    fun getMyPageUser() {
        viewModelScope.launch {
            kotlin.runCatching {
                myPageRepository.getMyPageUser().data
            }.onSuccess {
                //갑자기 strings 추가가 안되는데?
                _name.value = "${it.name}님은"
            }.onFailure {
                Timber.e("viewModel connect fail")
            }
        }
    }
}