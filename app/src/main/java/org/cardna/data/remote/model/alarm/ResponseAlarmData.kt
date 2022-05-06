package org.cardna.data.remote.model.alarm

data class ResponseAlarmData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
) {
    data class Data(
        val request: Request,
        val alarm: MutableList<Alarm>,
    )
}

data class Request(
    val count: Int,
    val requester: MutableList<Requester>
) {
    data class Requester(
        val id: Int,
        val name: String,
        val profileImage: String,
        val date: String,
    )
}

data class Alarm(
    val cardId: Int,
    val name: String,
    val content: String,
    val profileImage: String,
    val date: String
)

