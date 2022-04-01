package org.cardna.data.remote.model.friend


data class ResponseSearchFriendNameData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: MutableList<Data>,
) {
    data class Data(
        val id: Int,
        val name: String,
        val sentence: String?,
        val userImg: String
    )
}
