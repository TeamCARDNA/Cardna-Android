package org.cardna.data.remote.model.auth

data class ResponseTokenIssuanceData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val accessToken: String,
        val refreshToken: String
    )
}
