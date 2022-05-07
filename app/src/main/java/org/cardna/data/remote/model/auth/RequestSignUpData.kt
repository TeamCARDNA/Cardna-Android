package org.cardna.data.remote.model.auth

data class RequestSignUpData(
    val social: String,
    val uuid: String,
    val lastName: String,
    val firstName: String,
    val fcmToken: String,
)
