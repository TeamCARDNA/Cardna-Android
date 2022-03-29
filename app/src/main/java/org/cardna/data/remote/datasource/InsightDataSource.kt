package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.insight.ResponseInsightData

interface InsightDataSource {

    suspend fun getInsight(): ResponseInsightData
}