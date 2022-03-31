package org.cardna.presentation.ui.insight.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.insight.*
import org.cardna.domain.repository.InsightRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val insightRepository: InsightRepository
) : ViewModel() {

    private val _insight = MutableLiveData<ResponseInsightData.Data>()
    val insight: LiveData<ResponseInsightData.Data> = _insight

    private val _openAreaInsight = MutableLiveData<OpenArea>()
    val openAreaInsight: LiveData<OpenArea> = _openAreaInsight

    private val _blindAreaInsight = MutableLiveData<BlindArea>()
    val blindAreaInsight: LiveData<BlindArea> = _blindAreaInsight

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

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun getInsight() {
        viewModelScope.launch {
            runCatching {
                insightRepository.getInsight().data
            }.onSuccess {
                it.apply {
                    _blindAreaInsight.value = it.blindArea
                    _openAreaInsight.value = it.openArea

                    _isBlindAreaInsightEmpty.value = false
                    _isOpenAreaInsightEmpty.value = false

                    _blindAreaCardId.value = _blindAreaInsight.value?.id ?: return@launch
                    _openAreaCardId.value = _openAreaInsight.value?.id ?: return@launch
                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun setCurrentPosition(currentPosition: String) {
        _currentPosition.value = currentPosition
    }
}