package org.cardna.domain.repository

import org.cardna.domain.model.RepresentCardListData

interface CardRepository {
    suspend fun getRepresentCardList(): RepresentCardListData
}