package org.cardna.presentation.ui.detailcard.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.LikeRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val likeRepository: LikeRepository,
) : ViewModel() {

    private var id = savedStateHandle.get<Int>(BaseViewUtil.CARD_ID)

    private val _detailCard = MutableLiveData<ResponseDetailCardData.Data>()
    val detailCard: LiveData<ResponseDetailCardData.Data> = _detailCard

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _content = MutableLiveData("")
    val content: LiveData<String> = _content

    private val _createdAt = MutableLiveData("")
    val createdAt: LiveData<String> = _createdAt

    private val _isMineCard = MutableLiveData<Boolean>()
    val isMineCard: LiveData<Boolean> = _isMineCard

    private val _isStorage = MutableLiveData<Boolean>()
    val isStorage: LiveData<Boolean> = _isStorage

    private val _initLikeCount = MutableLiveData(0)
    val initLikeCount: MutableLiveData<Int?> = _initLikeCount

    var currentLikeCount = 4  //TODO 서버완성 후 다시 test

    /* 저장소 : storage true true
    * 내가 카드나 : me true false
    * 내가 카드너 : you true false
    * 너가 카드나 : me false false
    * 너가 카드너 : you false false*/

    fun setCardId(cardId: Int) {
        id = cardId
        getDetailCard()
    }

    private fun getDetailCard() {
        val cardId = id ?: return
        viewModelScope.launch {
            runCatching {
                cardRepository.getDetailCard(cardId).data
            }.onSuccess {
                it.apply {
                    _detailCard.value = it
                    _type.value = type
                    _title.value = title
                    _name.value = name
                    _content.value = content
                    _createdAt.value=createdAt

                    if (type == DetailCardActivity.STORAGE)
                        _isStorage.value = true
                    if (likeCount != null) {
                        _isMineCard.value = true
                        _initLikeCount.value = likeCount
                        currentLikeCount = likeCount
                    }

                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun postLike() {
        viewModelScope.launch {
            runCatching {
                likeRepository.postLike(RequestLikeData(id ?: return@launch))
            }.onSuccess {
                Timber.d(it.message)
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun deleteCard() {
        Timber.d("카드삭제")
        viewModelScope.launch {
            runCatching {
                //      cardRepository.deleteCard(id ?: return@launch)
            }.onSuccess {
                //       Timber.d(it.message)
            }.onFailure {
                //     Timber.e(it.toString())
            }
        }
    }

    fun keepOrAddCard() {
        Timber.d("카드보관")
        viewModelScope.launch {
            runCatching {
                //        cardRepository.putKeepOrAddCard(id ?: return@launch)
            }.onSuccess {
                //        Timber.d(it.message)
            }.onFailure {
                //        Timber.e(it.toString())
            }
        }
    }

    fun reportUser() {
        Timber.d("유저신고")
        viewModelScope.launch {
            runCatching {
            }.onSuccess {
            }.onFailure {
            }
        }
    }
}
