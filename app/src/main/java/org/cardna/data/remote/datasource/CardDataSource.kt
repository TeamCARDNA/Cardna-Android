package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.card.*

interface CardDataSource {

    suspend fun getDetailCard(cardId: Int): ResponseDetailCardData

    suspend fun deleteCard(cardId: Int): ResponseDeleteCardData

    suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData

    suspend fun getCardMe() : ResponseCardMeData

    suspend fun getOtherCardMe(cardId: Int) : ResponseCardMeData

    suspend fun getCardYou() : ResponseCardYouData

    suspend fun getOtherCardYou(cardId: Int) : ResponseCardYouData


}