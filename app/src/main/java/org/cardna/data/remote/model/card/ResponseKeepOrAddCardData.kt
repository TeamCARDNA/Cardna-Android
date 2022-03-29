package org.cardna.data.remote.model.card

data class ResponseKeepOrAddCardData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
) {
    data class Data(
        val title: String,
        val image: String
    )
}