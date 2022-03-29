package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.model.card.ResponseMainCardData
import javax.inject.Inject

class MainCardDataSourceImpl @Inject constructor(
    private val cardService: CardService
) : MainCardDataSource {
    override suspend fun getMainCard(): ResponseMainCardData {
        return cardService.getMainCard()
    }
}