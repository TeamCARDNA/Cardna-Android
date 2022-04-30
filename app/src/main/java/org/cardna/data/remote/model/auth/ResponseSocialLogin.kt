package org.cardna.data.remote.model.auth


data class ResponseSocialLogin(
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