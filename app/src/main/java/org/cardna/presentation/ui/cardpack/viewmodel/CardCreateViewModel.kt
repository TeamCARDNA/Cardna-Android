package org.cardna.presentation.ui.cardpack.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.cardna.data.remote.model.card.RequestCreateCardMeData
import org.cardna.data.remote.model.card.RequestCreateCardYouData
import org.cardna.data.remote.model.card.ResponseCardMeData
import org.cardna.data.remote.model.card.ResponseCardYouData
import org.cardna.domain.repository.CardRepository
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CardCreateViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() { // CardCreateActivity, BottomDialogImageFragment 에서 공유

    // 프로퍼티
    private var _id: Int? = null // 나의 카드나 작성일 경우 null, 친구의 카드너 작성일 경우 친구의 id값
    val id: Int?
        get() = _id

    private var _name: String? = null // 나의 카드나 작성일 경우 null, 친구의 카드너 작성일 경우 친구의 name값
    val name: String?
        get() = _name

    private var _isCardMeOrYou: Boolean? = null // 나의 카드나 작성일 경우 true, 친구의 카드너 작성일 경우 false
    val isCardMeOrYou: Boolean?
        get() = _isCardMeOrYou


    // 이 값들을 liveData로 바꿔서 이를 observe 할만한 게 있을까
    private var _symbolId: Int? = null // 이미지가 있는 경우 null로 보내줘야 함
    val symbolId: Int?
        get() = _symbolId

    private var _uri: Uri? = null // multipart 로 변환해서 서버에 img 로 보내줄 uri
    val uri: Uri?
        get() = _uri

    private var _imgIndex: Int? = GALLERY // 띄워줄
    val imgIndex: Int?
        get() = _imgIndex

    private var _ifChooseImg = MutableLiveData<Boolean>(false)// 갤러리 이미지나 심볼을 선택했는지 확인해주는 Bool 변수 => 나중에 버튼 enable 할때 사용
    val ifChooseImg: LiveData<Boolean>
        get() = _ifChooseImg


    // card title
    var _etKeywordText = MutableLiveData<String>(null)
    val etKeywordText: LiveData<String>
        get() = _etKeywordText

    // card title length
    private var _etKeywordLength = MutableLiveData<Int>(0)
    val etKeywordLength: LiveData<Int>
        get() = _etKeywordLength

    // card detail
    var _etDetailText = MutableLiveData<String>(null)
    val etDetailText: LiveData<String>
        get() = _etDetailText

    // card detail length
    private var _etDetailLength = MutableLiveData<Int>(0)
    val etDetailLength: LiveData<Int>
        get() = _etDetailLength


    // ******** 메서드 **********
    fun setUserId(id: Int) {
        if (id == -1)
            _id = null
        else
            _id = id
    }

    fun setUserName(name: String?) {
        _name = name
    }

    fun setIsCardMeOrYou(isCardMeOrYou: Boolean) {
        _isCardMeOrYou = isCardMeOrYou
    }

    fun setSymbolId(symbolId: Int?) {
        _symbolId = symbolId
    }

    fun setUri(uri: Uri?) {
        _uri = uri
    }

    fun setImgIndex(imgIndex: Int?) {
        _imgIndex = imgIndex
    }


    fun setIfChooseImg(ifChooseImg: Boolean) {
        _ifChooseImg.value = ifChooseImg
    }

    fun setEtKeywordLength(etKeywordLength: Int) {
        _etKeywordLength.value = etKeywordLength
    }

    fun setEtDetailLength(etDetailLength: Int) {
        _etDetailLength.value = etDetailLength
    }


    // 서버 통신 메서드

    // 카드 작성 method
    fun makeCard(makeUriToFile: MultipartBody.Part) { // Activity 에서 선택된 이미지 uri 만 multipart data 로 바꿔서 인자로 넣어줌
        if (id == null) {  // 카드나 작성 => friendId값 x
            val body = RequestCreateCardMeData(
                etKeywordText.value!!,
                etDetailText.value!!,
                symbolId // 갤러리 이미지를 선택했다면 dialog 완료 버튼을 누르지 않았을 테니까 null 값일 것임
            ).toRequestBody()

            if (uri == null) { // 심볼 선택
                viewModelScope.launch {
                    runCatching { cardRepository.postCreateCardMe(body, null) }
                        .onSuccess { Log.d("카드나 작성 성공", it.message) }
                        .onFailure { Log.d("카드나 작성 실패", it.message!!) }
                }
            } else { // 이미지 선택
                viewModelScope.launch {
                    runCatching { cardRepository.postCreateCardMe(body, makeUriToFile) }
                        .onSuccess { Log.d("카드나 작성 성공", it.message) }
                        .onFailure {
                            Log.d("카드나 작성 실패", it.message!!)
                            it.printStackTrace()
                        }
                }
            }
        } else { // 카드너 작성 => friendId 포함
            val body = RequestCreateCardYouData(
                etKeywordText.value!!,
                etDetailText.value!!,
                symbolId, // 갤러리 이미지를 선택했다면 dialog 완료 버튼을 누르지 않았을 테니까 null 값일 것임
                id
            ).toRequestBody()

            if (uri == null) { // 심볼 선택
                viewModelScope.launch {
                    runCatching { cardRepository.postCreateCardMe(body, null) }
                        .onSuccess { Log.d("카드너 작성 성공", it.message) }
                        .onFailure { Log.d("카드너 작성 실패", it.message!!) }
                }
            } else { // 이미지 선택
                viewModelScope.launch {
                    runCatching { cardRepository.postCreateCardMe(body, makeUriToFile) }
                        .onSuccess { Log.d("카드너 작성 성공", it.message) }
                        .onFailure {
                            Log.d("카드너 작성 실패", it.message!!)
                            it.printStackTrace()
                        }
                }
            }
        }
    }

    companion object {
        const val SYMBOL_0 = 0
        const val SYMBOL_1 = 1
        const val SYMBOL_2 = 2
        const val SYMBOL_3 = 3
        const val SYMBOL_4 = 4
        const val GALLERY = 5
    }
}
