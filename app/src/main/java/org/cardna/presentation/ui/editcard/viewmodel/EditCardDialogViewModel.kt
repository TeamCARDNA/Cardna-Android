package org.cardna.presentation.ui.editcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.CardData
import org.cardna.data.remote.model.card.MainCard
import org.cardna.domain.repository.CardRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditCardDialogViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    private val _cardMeList = MutableLiveData<List<CardData>>()
    val cardMeList: LiveData<List<CardData>> = _cardMeList

    private val _cardYouList = MutableLiveData<List<CardData>>()
    val cardYouList: LiveData<List<CardData>> = _cardYouList

    private val _selectedCardList = MutableLiveData<List<Int>>()
    val selectedCardList: LiveData<List<Int>> = _selectedCardList

    private val _mainCardList = MutableLiveData<List<MainCard>>()
    val mainCardList: LiveData<List<MainCard>> = _mainCardList

    fun getMainCard() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getMainCard().data
            }.onSuccess {
                _mainCardList.value = it.mainCardList
            }.onFailure {

            }
        }
    }

    fun getCardAll() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getCardAll().data
            }.onSuccess {
                _cardMeList.value = it.cardMeList
                _cardYouList.value = it.cardYouList
                Timber.d("get cardAll success")
            }.onFailure {
                Timber.e("get cardAll error")
            }
        }
    }

    fun representCardCheck() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getMainCard().data
            }.onSuccess {
                _selectedCardList.value = it.mainCardList.map { it.id }
                Timber.d("selected list : ${_selectedCardList.value}")
            }.onFailure {
                Timber.e("get main card error")
            }
        }
    }

    fun setChangeSelectedList(selectedList: MutableList<Int>) {
        _selectedCardList.value = selectedList
        Timber.d("selectedCardList : ${_selectedCardList.value}")
    }
}