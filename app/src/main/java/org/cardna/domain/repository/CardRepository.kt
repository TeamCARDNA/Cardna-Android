package org.cardna.domain.repository

import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData

interface CardRepository {

    suspend fun getDetailCard(cardId: Int): ResponseDetailCardData

    suspend fun deleteCard(cardId: Int): ResponseDeleteCardData

    suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData

    suspend fun getCardMe(): ResponseCardPackData

    suspend fun getOtherCardMe(cardId: Int): ResponseCardPackData

    suspend fun getCardYou(): ResponseCardPackData

    suspend fun getOtherCardYou(cardId: Int): ResponseCardPackData

    suspend fun getMainCard() : ResponseMainCardData
}