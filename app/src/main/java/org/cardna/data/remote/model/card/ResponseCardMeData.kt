package org.cardna.data.remote.model.card

data class ResponseCardMeData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: CardList
) {
    data class CardList(
        val totalCardCnt: Int,
        val isMyCard: Boolean,
        val cardMeList: MutableList<CardMe>
    ){
        data class CardMe(
            val id: Int,
            val cardImg: String,
            val title: String,
            val mainOrder: Int,
            val isLiked: Boolean
        )
    }
}
