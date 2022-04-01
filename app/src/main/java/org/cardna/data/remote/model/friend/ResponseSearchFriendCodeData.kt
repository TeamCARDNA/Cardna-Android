package org.cardna.data.remote.model.friend

class ResponseSearchFriendCodeData(

    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val id: Int,
        val name: String,
        val relationType: Int,
        val userImg: String,
        val code: String
    )
}