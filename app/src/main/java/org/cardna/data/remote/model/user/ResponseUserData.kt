package org.cardna.data.remote.model.user

data class ResponseUserData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val social: String,
        val acceptPush: Boolean,
    )
}