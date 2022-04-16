package org.cardna.presentation.ui.maincard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.MainCard
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
                    it.mainCardList.map {
                        _cardId.value = it.id
                    }
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

    fun saveInitCardPosition(cardPosition: Int) {
        _cardPosition.value = cardPosition
    }
}