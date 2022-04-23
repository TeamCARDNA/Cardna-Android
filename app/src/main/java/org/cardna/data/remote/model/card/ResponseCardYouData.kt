package org.cardna.data.remote.model.card

data class ResponseCardYouData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: CardList
) {
    data class CardList(
        val totalCardCnt: Int,
        val isMyCard: Boolean,
        val cardYouList: MutableList<CardYou>
    ){
        data class CardYou(
            val id: Int,
            val cardImg: String,
            val title: String,
            val mainOrder: Int?,
            val isLiked: Boolean?
        )
    }
}
