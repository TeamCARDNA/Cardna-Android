package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.alarm.ResponsePutAlarmData

interface AlarmDataSource {
    suspend fun getAlarm(): ResponseGetAlarmData

    suspend fun putAlarm(): ResponsePutAlarmData
}