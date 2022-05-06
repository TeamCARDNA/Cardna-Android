package org.cardna.domain.repository

import org.cardna.data.remote.model.alarm.ResponseAlarmData

interface AlarmRepository {
    suspend fun getAlarm(): ResponseAlarmData
}