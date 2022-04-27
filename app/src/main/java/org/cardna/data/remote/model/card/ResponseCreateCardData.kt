package org.cardna.data.remote.model.card

data class ResponseCreateCardData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
){
    data class Data(
        val id: Int,
        val title: String,
        val image: String
    )
}
