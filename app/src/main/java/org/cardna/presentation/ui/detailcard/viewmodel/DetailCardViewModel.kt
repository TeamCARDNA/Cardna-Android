package org.cardna.presentation.ui.detailcard.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.cardna.data.local.dao.DeletedCardYouDao
import org.cardna.data.local.entity.DeletedCardYouData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.user.RequestPostReportUserData
import org.cardna.domain.repository.CardRepository
import org.cardna.domain.repository.LikeRepository
import org.cardna.domain.repository.UserRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val deletedCardYouDao: DeletedCardYouDao
) : ViewModel() {

    private var cardId = savedStateHandle.get<Int>(BaseViewUtil.CARD_ID)

    private val _detailCard = MutableLiveData<ResponseDetailCardData.Data>()
    val detailCard: LiveData<ResponseDetailCardData.Data> = _detailCard

    private val _cardImg = MutableLiveData<String>()
    val cardImg: LiveData<String> = _cardImg

    private val _writerId = MutableLiveData<Int>()
    val writerId: LiveData<Int> = _writerId

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _createAt = MutableLiveData<String>()
    val createAt: LiveData<String> = _createAt

    private val _content = MutableLiveData<String>()
    val content: LiveData<String> = _content

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _isMineCard = MutableLiveData<Boolean>()
    val isMineCard: LiveData<Boolean> = _isMineCard

    private val _isStorage = MutableLiveData<Boolean>()
    val isStorage: LiveData<Boolean> = _isStorage

    private val _initLikeCount = MutableLiveData<Int?>(0)
    val initLikeCount: MutableLiveData<Int?> = _initLikeCount

    var currentLikeCount: Int = 0

    val myDefault = MutableLiveData("")


    /* 저장소 : storage true true
    * 내가 카드나 : me true false
    * 내가 카드너 : you true false
    * 너가 카드나 : me false false
    * 너가 카드너 : you false false*/

    fun setCardId(cardId: Int) {
        this.cardId = cardId
        getDetailCard()
    }

    private fun getDetailCard() {
        val cardId = cardId ?: return
        viewModelScope.launch {
            runCatching {
                cardRepository.getDetailCard(cardId).data
            }.onSuccess {
                it.apply {
                    _detailCard.value = it
                    _cardImg.value = cardImg
                    _type.value = type
                    _title.value = title
                    _name.value = name ?: ""
                    _createAt.value = createdAt
                    _content.value = content
                    _writerId.value = writerId
                    _initLikeCount.value = likeCount
                    currentLikeCount = likeCount ?: 0
                    _isMineCard.value = isLiked == null

                    if (type == DetailCardActivity.STORAGE) _isStorage.value = true

                    if (isLiked != null) _isLiked.value = isLiked!!

                }
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun postLike() {
        viewModelScope.launch {
            runCatching {
                likeRepository.postLike(RequestLikeData(cardId ?: return@launch))
            }.onSuccess {
                Timber.d(it.message)
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun deleteCard() {
        val cardId = cardId ?: return
        viewModelScope.launch {
            runCatching {
                cardRepository.deleteCard(cardId)
            }.onSuccess {
                Timber.d(it.message)
                Log.d("ㅡㅡㅡㅡㅡㅡ삭제되고 룸에저장ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "${cardId}")
                deletedCardYouDao.insertDeletedCardYou(DeletedCardYouData(cardId))
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun keepOrAddCard() {
        val cardId = cardId ?: return
        viewModelScope.launch {
            runCatching {
                cardRepository.putKeepOrAddCard(cardId)
            }.onSuccess {
                Timber.d(it.message)
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun reportUser(reportReason: String) {
        val cardId = cardId ?: return
        val writerId = _writerId.value ?: return
        viewModelScope.launch {
            runCatching {
                userRepository.postReportUser(RequestPostReportUserData(writerId, cardId, reportReason))
            }.onSuccess {
                Timber.d(it.message)
            }.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}
