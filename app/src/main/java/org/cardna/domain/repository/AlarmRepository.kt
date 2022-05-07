package org.cardna.domain.repository

import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.alarm.ResponsePutAlarmData

interface AlarmRepository {
    suspend fun getAlarm(): ResponseGetAlarmData

    suspend fun putAlarm(): ResponsePutAlarmData
}