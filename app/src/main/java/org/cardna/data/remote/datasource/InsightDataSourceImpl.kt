package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.insight.InsightService
import org.cardna.data.remote.model.insight.ResponseInsightData
import javax.inject.Inject

class InsightDataSourceImpl @Inject constructor(
    private val insightService: InsightService
) : InsightDataSource {

    override suspend fun getInsight(): ResponseInsightData {
       return insightService.getInsight()
    }
}