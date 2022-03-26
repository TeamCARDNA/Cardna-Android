package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.card.ResponseDetailCardData

interface CardDataSource {

    suspend fun getDetailCard(cardId: Int): ResponseDetailCardData
}