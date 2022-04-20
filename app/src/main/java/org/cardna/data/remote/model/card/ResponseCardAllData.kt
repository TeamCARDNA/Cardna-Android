package org.cardna.data.remote.model.card

data class ResponseCardAllData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
){
    data class Data(
        val totalCardCnt: Int,
        val isMyCard: Boolean,
        val cardMeList: List<CardMe>,
        val cardYouList: List<CardYou>
    ){
        data class CardMe(
            val id: Int,
            val title: String,
            val cardImg: String,
            val mainOrder: Int,
            val isLiked: Boolean
        )

        data class CardYou(
            val id: Int,
            val title: String,
            val cardImg: String,
            val mainOrder: Int,
            val isLiked: Boolean
        )

    }
}
