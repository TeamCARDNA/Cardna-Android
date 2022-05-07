package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.alarm.AlarmService
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.alarm.ResponsePutAlarmData
import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val alarmService: AlarmService
) : AlarmDataSource {

    override suspend fun getAlarm(): ResponseGetAlarmData {
        return alarmService.getAlarm()
    }

    override suspend fun putAlarm(): ResponsePutAlarmData {
        return alarmService.putAlarm()
    }
}