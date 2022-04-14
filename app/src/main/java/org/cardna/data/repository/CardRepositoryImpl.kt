package org.cardna.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.data.remote.datasource.CardDataSource
import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDataSource: CardDataSource) :
    CardRepository {

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardDataSource.getDetailCard(cardId)
    }

    override suspend fun deleteCard(cardId: Int): ResponseDeleteCardData {
        return cardDataSource.deleteCard(cardId)
    }

    override suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData {
        return cardDataSource.putKeepOrAddCard(cardId)
    }

    override suspend fun getCardMe(): ResponseCardMeData {
        return cardDataSource.getCardMe()
    }

    override suspend fun getOtherCardMe(userId: Int): ResponseCardMeData {
        return cardDataSource.getOtherCardMe(userId)
    }

    override suspend fun getCardYou(): ResponseCardYouData {
        return cardDataSource.getCardYou()
    }

    override suspend fun getOtherCardYou(userId: Int): ResponseCardYouData {
        return cardDataSource.getOtherCardYou(userId)
    }

    override suspend fun postCreateCardMe(
        body: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ) : ResponseCreateCardData {
        return cardDataSource.postCreateCardMe(body, image)
    }

    override suspend fun getMainCard(): ResponseMainCardData {
        return cardDataSource.getMainCard()
    }
}
