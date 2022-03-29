package org.cardna.data.remote.api.insight

import org.cardna.data.remote.model.insight.ResponseInsightData
import retrofit2.http.GET

interface InsightService {

    @GET("insight")
    suspend fun getInsight(): ResponseInsightData
}