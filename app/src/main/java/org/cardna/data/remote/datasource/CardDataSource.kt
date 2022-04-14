package org.cardna.data.remote.datasource

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData

interface CardDataSource {
    suspend fun getDetailCard(cardId: Int): ResponseDetailCardData

    suspend fun deleteCard(cardId: Int): ResponseDeleteCardData

    suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData

    suspend fun getCardMe() : ResponseCardMeData

    suspend fun getOtherCardMe(userId: Int) : ResponseCardMeData

    suspend fun getCardYou() : ResponseCardYouData

    suspend fun getOtherCardYou(userId: Int) : ResponseCardYouData

    suspend fun postCreateCardMe(body: HashMap<String, RequestBody>, image: MultipartBody.Part?) : ResponseCreateCardData

    suspend fun getMainCard() : ResponseMainCardData
}