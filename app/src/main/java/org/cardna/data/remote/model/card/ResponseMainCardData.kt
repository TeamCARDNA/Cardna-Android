package org.cardna.data.remote.model.card


data class ResponseMainCardData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val hasNotification: Boolean,
        val isBlocked: Boolean,
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

