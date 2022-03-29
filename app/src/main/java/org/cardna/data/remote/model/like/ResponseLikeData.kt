package org.cardna.data.remote.model.like

data class ResponseLikeData(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data,
) {
    data class Data(
        val isLiked: Boolean,
    )
}