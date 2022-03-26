package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.model.card.ResponseDetailCardData
import javax.inject.Inject

class CardDataSourceImpl @Inject constructor(
    private val cardService: CardService
) : CardDataSource {

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardService.getDetailCard(cardId)
    }
}