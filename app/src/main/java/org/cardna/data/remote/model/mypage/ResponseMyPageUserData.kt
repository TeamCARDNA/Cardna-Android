package org.cardna.data.remote.model.mypage

data class ResponseMyPageUserData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val code: String,
        val id: Int,
        val name: String,
        val userImg: String
    )
}