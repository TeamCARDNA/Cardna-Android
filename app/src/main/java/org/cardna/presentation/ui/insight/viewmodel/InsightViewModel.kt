package org.cardna.presentation.ui.insight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.insight.BlindAreaCard
import org.cardna.data.remote.model.insight.OpenAreaCard
import org.cardna.data.remote.model.insight.ResponseInsightData
import org.cardna.domain.repository.InsightRepository
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val insightRepository: InsightRepository
) : ViewModel() {

    private val _insight = MutableLiveData<ResponseInsightData.Data>()
    val insight: LiveData<ResponseInsightData.Data> = _insight

    private val _openAreaInsight = MutableLiveData<OpenAreaCard>()
    val openAreaInsight: LiveData<OpenAreaCard> = _openAreaInsight

    private val _blindAreaInsight = MutableLiveData<BlindAreaCard>()
    val blindAreaInsight: LiveData<BlindAreaCard> = _blindAreaInsight

    private val _isOpenAreaInsightEmpty = MutableLiveData<Boolean>()
    val isOpenAreaInsightEmpty: LiveData<Boolean> = _isOpenAreaInsightEmpty

    private val _isBlindAreaInsightEmpty = MutableLiveData<Boolean>()
    val isBlindAreaInsightEmpty: LiveData<Boolean> = _isBlindAreaInsightEmpty

    private val _blindAreaCardId = MutableLiveData<Int>()
    val blindAreaCardId: LiveData<Int> = _blindAreaCardId

    private val _openAreaCardId = MutableLiveData<Int>()
    val openAreaCardId: LiveData<Int> = _openAreaCardId

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    fun getInsight() {
        viewModelScope.launch {
            runCatching {
                insightRepository.getInsight().data
            }.onSuccess {
                _blindAreaInsight.value = it.blindAreaCard
                _openAreaInsight.value = it.openAreaCard

                _isBlindAreaInsightEmpty.value = _blindAreaInsight.value == null
                _isOpenAreaInsightEmpty.value = _openAreaInsight.value == null

                _blindAreaCardId.value = _blindAreaInsight.value?.id ?: 0
                _openAreaCardId.value = _openAreaInsight.value?.id ?: 0
            }.onFailure {

            }
        }
    }

    fun setCurrentPosition(currentPosition: String) {
        _currentPosition.value = currentPosition
    }
}