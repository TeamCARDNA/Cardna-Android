package org.cardna.data.remote.datasource

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

    override suspend fun getCardMe(): ResponseCardPackData {
        return cardService.getCardMe()
    }

    override suspend fun getOtherCardMe(cardId: Int): ResponseCardPackData {
        return cardService.getOtherCardMe(cardId)
    }

    override suspend fun getCardYou(): ResponseCardPackData {
        return cardService.getCardYou()
    }

    override suspend fun getOtherCardYou(cardId: Int): ResponseCardPackData {
        return cardService.getOtherCardYou(cardId)
    }

    override suspend fun getMainCard(): ResponseMainCardData {
        return cardService.getMainCard()

    }
}