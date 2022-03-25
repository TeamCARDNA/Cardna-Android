package org.cardna.data.repository

import org.cardna.data.remote.datasource.InsightDataSource
import org.cardna.data.remote.model.insight.ResponseInsightData
import org.cardna.domain.repository.InsightRepository
import javax.inject.Inject

class InsightRepositoryImpl @Inject constructor(
    private val insightDataSource: InsightDataSource
) : InsightRepository {

    override suspend fun getInsight(): ResponseInsightData {
        return insightDataSource.getInsight()
    }
}