package org.cardna.data.remote.model.user

data class ResponsePostReportUserData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val id: Int,
        val firstName: String,
        val reportCount: Int
    )
}