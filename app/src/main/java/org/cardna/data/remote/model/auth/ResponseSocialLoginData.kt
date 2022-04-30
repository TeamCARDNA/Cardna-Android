package org.cardna.data.remote.model.auth


data class ResponseSocialLoginData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data
) {
    data class Data(
        val social: String,
        val type: String,
        val uuid: String
    )
}