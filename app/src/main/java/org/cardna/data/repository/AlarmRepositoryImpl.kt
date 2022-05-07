package org.cardna.data.repository

import org.cardna.data.remote.datasource.AlarmDataSource
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.alarm.ResponsePutAlarmData
import org.cardna.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDataSource: AlarmDataSource
) : AlarmRepository {

    override suspend fun getAlarm(): ResponseGetAlarmData {
        return alarmDataSource.getAlarm()
    }

    override suspend fun putAlarm(): ResponsePutAlarmData {
        return alarmDataSource.putAlarm()
    }
}