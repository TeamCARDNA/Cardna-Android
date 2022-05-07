package org.cardna.data.remote.api.alarm

import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.alarm.ResponsePutAlarmData
import retrofit2.http.GET
import retrofit2.http.PUT

interface AlarmService {
    @GET("alarm")
    suspend fun getAlarm(): ResponseGetAlarmData

    @PUT("alarm")
    suspend fun putAlarm(): ResponsePutAlarmData
}