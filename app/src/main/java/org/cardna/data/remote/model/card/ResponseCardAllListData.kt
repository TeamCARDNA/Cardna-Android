package org.cardna.data.remote.model.card

data class ResponseCardAllListData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
) {
    data class Data(
        val totalCardCnt: Int,
        val isMyCard: Boolean,
        val cardMeList: List<CardData>,
        val cardYouList: List<CardData>
    )
}

data class CardData(
    val id: Int,
    val title: String,
    val cardImg: String,
    val mainOrder: Int,
    val isLiked: Boolean,
    var isMe : Boolean = false
)
