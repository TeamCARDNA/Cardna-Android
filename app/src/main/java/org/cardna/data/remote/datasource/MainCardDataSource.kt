package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.card.ResponseMainCardData

interface MainCardDataSource {
    suspend fun getMainCard(): ResponseMainCardData
}