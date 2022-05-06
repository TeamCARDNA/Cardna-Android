package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.alarm.ResponseAlarmData

interface AlarmDataSource {
    suspend fun getAlarm(): ResponseAlarmData
}