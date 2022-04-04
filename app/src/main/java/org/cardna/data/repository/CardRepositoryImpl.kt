package org.cardna.data.repository

import org.cardna.data.remote.datasource.CardDataSource
import org.cardna.data.remote.model.card.*
import org.cardna.data.remote.mapper.CardToRepresentCardListMapper
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.model.RepresentCardListData
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

    override suspend fun getOtherCardMe(cardId: Int): ResponseCardMeData {
        return cardDataSource.getOtherCardMe(cardId)
    }

    override suspend fun getCardYou(cardId: Int?): ResponseCardYouData {
        return cardDataSource.getCardYou(cardId)
    }

    override suspend fun getMainCard(): ResponseMainCardData {
        return cardDataSource.getMainCard()

    }
}
