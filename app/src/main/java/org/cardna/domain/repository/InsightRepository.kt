package org.cardna.domain.repository

import org.cardna.data.remote.model.insight.ResponseInsightData

interface InsightRepository {

    suspend fun getInsight(): ResponseInsightData
}