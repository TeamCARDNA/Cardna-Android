package org.cardna.presentation.ui.editcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.CardData
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
}