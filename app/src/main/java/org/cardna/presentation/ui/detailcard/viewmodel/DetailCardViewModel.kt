package org.cardna.presentation.ui.detailcard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _detailCard = MutableLiveData<ResponseDetailCardData.Data>()
    val detailCard: LiveData<ResponseDetailCardData.Data> = _detailCard

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _isMineCard = MutableLiveData(false)
    val isMineCard: LiveData<Boolean> = _isMineCard

    private val _isStorage = MutableLiveData(true)
    val isStorage: LiveData<Boolean> = _isStorage

    fun getDetailCard(id: Int) {
        viewModelScope.launch {
            runCatching {
                cardRepository.getDetailCard(id).data
            }.onSuccess {
                _detailCard.value = it
                _type.value = it.type
                if (it.type == DetailCardActivity.STORAGE) _isStorage.value = true
                if (it.likeCount != null) _isMineCard.value = true
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}
