package org.cardna.domain.repository

import org.cardna.data.remote.model.card.ResponseDetailCardData

interface CardRepository {

    suspend fun getDetailCard(cardId: Int): ResponseDetailCardData
}