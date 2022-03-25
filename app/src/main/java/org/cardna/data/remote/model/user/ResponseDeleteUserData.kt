package org.cardna.data.remote.model.user

data class ResponseDeleteUserData(
    val status: Int,
    val success: Boolean,
    val message: String,
)
