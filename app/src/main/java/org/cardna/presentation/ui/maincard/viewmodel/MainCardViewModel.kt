package org.cardna.presentation.ui.maincard.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.base.BaseViewUtil
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _cardImg = MutableLiveData<String>()
    val cardImg: LiveData<String> = _cardImg

    private val _isMe = MutableLiveData<Boolean>()
    val isMe: LiveData<Boolean> = _isMe

    private val _mainOrder = MutableLiveData<Int>()
    val mainOrder: LiveData<Int> = _mainOrder

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun getMainCardList() {
        viewModelScope.launch {
            kotlin.runCatching {
                cardRepository.getMainCard().data.mainCardList
            }.onSuccess { it ->
                it.map {
                    _cardImg.value = it.cardImg
                    _id.value = it.id
                    _isMe.value = it.isMe
                    _mainOrder.value = it.mainOrder
                    _title.value = it.title
                }
            }.onFailure {

            }
        }
    }
}