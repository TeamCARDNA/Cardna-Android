package org.cardna.data.remote.api.alarm

import org.cardna.data.remote.model.alarm.ResponseAlarmData
import retrofit2.http.GET

interface AlarmService {
    @GET("alarm")
    suspend fun getAlarm(): ResponseAlarmData
}