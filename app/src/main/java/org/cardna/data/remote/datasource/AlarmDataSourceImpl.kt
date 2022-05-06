package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.alarm.AlarmService
import org.cardna.data.remote.model.alarm.ResponseAlarmData
import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val alarmService: AlarmService
) : AlarmDataSource {

    override suspend fun getAlarm(): ResponseAlarmData {
        return alarmService.getAlarm()
    }
}