package org.cardna.data.repository

import org.cardna.data.remote.datasource.MainCardDataSource
import org.cardna.data.remote.mapper.CardToRepresentCardListMapper
import org.cardna.domain.model.RepresentCardListData
import org.cardna.domain.repository.CardRepository
import javax.inject.Inject
import org.cardna.data.remote.model.card.ResponseDeleteCardData
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.card.ResponseKeepOrAddCardData

class CardRepositoryImpl @Inject constructor(private val cardDataSource: MainCardDataSource) :
    CardRepository {

    override suspend fun getRepresentCardList(): RepresentCardListData {
        return CardToRepresentCardListMapper.mainCardToRepresent(cardDataSource.getMainCard())
    }

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardDataSource.getDetailCard(cardId)
    }

    override suspend fun deleteCard(cardId: Int): ResponseDeleteCardData {
        return cardDataSource.deleteCard(cardId)
    }

    override suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData {
        return cardDataSource.putKeepOrAddCard(cardId)
    }
}