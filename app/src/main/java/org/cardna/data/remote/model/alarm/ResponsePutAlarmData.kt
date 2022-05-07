package org.cardna.data.remote.model.alarm

data class ResponsePutAlarmData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val acceptPush: Boolean
    )
}