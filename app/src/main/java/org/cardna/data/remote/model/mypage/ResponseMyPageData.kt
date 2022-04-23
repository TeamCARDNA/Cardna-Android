package org.cardna.data.remote.model.mypage

data class ResponseMyPageData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val id: Int,
        val name: String,
        val userImg: String,
        val code: String,
        val friendList: MutableList<FriendList>,
    ) {
        data class FriendList(
            val id: Int,
            val name: String,
            val userImg: String,
            val sentence: String?,
        )
    }
}
