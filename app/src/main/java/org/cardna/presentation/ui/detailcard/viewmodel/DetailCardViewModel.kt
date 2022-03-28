package org.cardna.presentation.ui.detailcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _detailCard = MutableLiveData<ResponseDetailCardData.Data>()
    val detailCard: LiveData<ResponseDetailCardData.Data> = _detailCard

    private val _type = MutableLiveData<String>("you")
    val type: LiveData<String> = _type

    private val _isMineCard = MutableLiveData(true)
    val isMineCard: LiveData<Boolean> = _isMineCard

    private val _isStorage = MutableLiveData(false)
    val isStorage: LiveData<Boolean> = _isStorage

    var likeCount = 4

    /* 저장소 : storage true true
    * 내가 카드나 : me true false
    * 내가 카드너 : you true false
    * 너가 카드나 : me false false
    * 너가 카드너 : you false false*/


    fun getDetailCard(id: Int) {
        viewModelScope.launch {
            runCatching {
                cardRepository.getDetailCard(id).data
            }.onSuccess {
                _detailCard.value = it
                _type.value = it.type
                if (it.type == DetailCardActivity.STORAGE) _isStorage.value = true
                if (it.likeCount != null) {
                    _isMineCard.value = true
                    likeCount = it.likeCount
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}
