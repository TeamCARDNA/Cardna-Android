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
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _detailCard = MutableLiveData<ResponseDetailCardData.Data>()
    val detailCard: LiveData<ResponseDetailCardData.Data> = _detailCard

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    fun getDetailCard(id: Int) {
        viewModelScope.launch {
            runCatching {
                cardRepository.getDetailCard(id).data
            }.onSuccess {
                _detailCard.value = it
                _title.value = it.title
            }.onFailure {
                Log.e("상세카드 조회 에러", it.toString())
            }
        }
    }
}