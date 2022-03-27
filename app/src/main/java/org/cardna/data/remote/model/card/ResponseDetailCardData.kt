package org.cardna.data.remote.model.card

data class ResponseDetailCardData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val id: String,
        val type: String,
        val cardImg: String?,
        val title: String,
        val content: String,
        val name: String?,
        val createdAt: String,
        val isLiked: Boolean?,
        val likeCount: Int?
    )
}
