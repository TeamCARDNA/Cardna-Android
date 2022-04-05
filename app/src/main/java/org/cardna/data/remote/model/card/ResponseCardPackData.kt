package org.cardna.data.remote.model.card

import com.google.gson.annotations.SerializedName

data class ResponseCardPackData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: CardList
) {
    data class CardList(
        val totalCardCnt: Int,
        val isMyCard: Boolean,
        val cardList: MutableList<Card>
    ){
        data class Card(
            val id: Int,
            val cardImg: String,
            val title: String,
            val mainOrder: Int,
            val isLiked: Boolean
        )
    }
}
