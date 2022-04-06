package org.cardna.data.remote.model.card


import com.google.gson.annotations.SerializedName

data class ResponseEditCardData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data
) {
    data class Data(
        val isMyCard: Boolean,
        val relation: Any,
        val mainCardList: List<MainCard>,
    ) {
        data class MainCard(
            val cardImg: String,
            val id: Int,
            val isMe: Boolean,
            val mainOrder: Int,
            val title: String
        )
    }
}