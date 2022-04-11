package org.cardna.data.remote.model.card


data class ResponseCardAllData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val isMyCard: Boolean,
        val totalCardCnt: Int,
        val cardMeList: List<CardData>,
        val cardYouList: List<CardData>,
    )
}

data class CardData(
    val cardImg: String,
    val id: Int,
    val isLiked: Any,
    val mainOrder: Any,
    val title: String,
    var isMe: Boolean,
)