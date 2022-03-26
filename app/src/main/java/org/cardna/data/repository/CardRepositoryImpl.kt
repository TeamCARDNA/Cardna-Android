package org.cardna.data.repository

import org.cardna.data.remote.datasource.CardDataSource
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardDataSource: CardDataSource
) : CardRepository {

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardDataSource.getDetailCard(cardId)
    }
}