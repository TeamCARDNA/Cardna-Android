package org.cardna.presentation.ui.editcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.CardData
import org.cardna.data.remote.model.card.MainCard
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.repository.CardRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _mainCardList = MutableLiveData<List<MainCard>>()
    val mainCardList: LiveData<List<MainCard>> = _mainCardList

    private val _cardMeList = MutableLiveData<List<CardData>>()
    val cardMeList: LiveData<List<CardData>> = _cardMeList

    private val _cardYouList = MutableLiveData<List<CardData>>()
    val cardYouList: LiveData<List<CardData>> = _cardYouList

    private val _selectedCardList = MutableLiveData<MutableList<Int>>()
    val selectedCardList: LiveData<MutableList<Int>> = _selectedCardList

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> = _currentPosition

    private val _isMainCardEmpty = MutableLiveData<Boolean>()
    val isMainCardEmpty: LiveData<Boolean> = _isMainCardEmpty

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun getMainCard() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getMainCard().data
            }.onSuccess {
                _mainCardList.value = it.mainCardList
                _isMainCardEmpty.value = it.mainCardList.isEmpty()
                Timber.d("get_main_card_success")
            }.onFailure {
                Timber.e("get_main_card_error")
            }
        }
    }

    fun putEditCard(cards: RequestEditCardData) {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.putEditCard(cards).data.mainCardList
            }.onSuccess {
                _mainCardList.value = it
                _isMainCardEmpty.value = it.isEmpty()
                _isSuccess.value = true
            }.onFailure {
                Timber.e("put_edit_card_error")
            }
        }
    }

    fun getCardAll() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getCardAllList().data
            }.onSuccess {
                _cardMeList.value = it.cardMeList
                _cardYouList.value = it.cardYouList
                Timber.d("get cardAll success")
            }.onFailure {
                Timber.e("get cardAll error")
            }
        }
    }

    fun setChangeSelectedList(selectedList: MutableList<Int>) {
        _selectedCardList.value = selectedList //수정에서 삭제한 애들 남긴 선택된카드리스트갱신
        _isMainCardEmpty.value = selectedList.isEmpty()
        Timber.d("selectedCardList : ${_selectedCardList.value}")
    }

    fun setDeleteCard(id: Int) {
        _selectedCardList.value?.remove(id)
        Timber.d("삭제")
        _selectedCardList.value = _selectedCardList.value
    }

    fun setAddCard(id: Int) {
        _selectedCardList.value?.add(id)
        Timber.d("추가")
        _selectedCardList.value = _selectedCardList.value
    }

    fun getSelectedCardListNullCheck(): Boolean {
        return _selectedCardList.value.isNullOrEmpty()
    }

    fun setChangeMainCardList(mainCardList: MutableList<MainCard>) {
        _mainCardList.value = mainCardList
        _isMainCardEmpty.value = mainCardList.isEmpty()
    }
}