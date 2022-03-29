package org.cardna.data.remote.model.card

data class ResponseDeleteCardData(
    val status: Int,
    val success: Boolean,
    val message: String
)