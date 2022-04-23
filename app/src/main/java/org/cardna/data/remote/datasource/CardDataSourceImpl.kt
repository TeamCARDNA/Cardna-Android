package org.cardna.data.remote.datasource

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import javax.inject.Inject

class CardDataSourceImpl @Inject constructor(
    private val cardService: CardService
) : CardDataSource {

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardService.getDetailCard(cardId)
    }

    override suspend fun deleteCard(cardId: Int): ResponseDeleteCardData {
        return cardService.deleteCard(cardId)
    }

    override suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData {
        return cardService.putKeepOrAddCard(cardId)
    }


    override suspend fun getCardMe(): ResponseCardMeData {
        return cardService.getCardMe()
    }

    override suspend fun getOtherCardMe(userId: Int): ResponseCardMeData {
        return cardService.getOtherCardMe(userId)
    }

    override suspend fun getCardAllList(): ResponseCardAllListData {
        return cardService.getCardAllList()
    }

    override suspend fun getCardYou(): ResponseCardYouData {
        return cardService.getCardYou()
    }

    override suspend fun getOtherCardYou(userId: Int): ResponseCardYouData {
        return cardService.getOtherCardYou(userId)
    }

    override suspend fun postCreateCardMe(
        body: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): ResponseCreateCardData {
        return cardService.postCreateCardMe(body, image)
    }

    override suspend fun getMainCard(): ResponseMainCardData {
        return cardService.getMainCard()
    }

    override suspend fun putEditCard(cards: RequestEditCardData): ResponseEditCardData {
        return cardService.putEditCard(cards)
    }

    override suspend fun getCardAll(): ResponseCardAllData {
        return cardService.getCardAll()
    }

    override suspend fun getCardYouStore(): ResponseCardYouStoreData {
        return cardService.getCardYouStore()
    }

    override suspend fun getOtherMainCard(userId: Int): ResponseMainCardData {
        return cardService.getOtherMainCard(userId)
    }
}