package org.cardna.presentation.ui.editcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.repository.CardRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _mainCardList = MutableLiveData<List<ResponseMainCardData.Data.MainCard>>()
    val mainCardList: LiveData<List<ResponseMainCardData.Data.MainCard>> = _mainCardList

    fun getMainCard() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getMainCard().data
            }.onSuccess {
                _mainCardList.value = it.mainCardList
            }.onFailure {
                Timber.e("get_main_card_error")
            }
        }
    }

//    fun putEditCard(cardList: RequestEditCardData) {
//        viewModelScope.launch {
//            kotlin.runCatching {
//                cardRepository.putEditCard(cardList)
//            }.onSuccess {
//                Timber.d("put_edit_card success")
//            }.onFailure {
//                Timber.e("put_edit_card_error")
//            }
//        }
//    }
}