package org.cardna.data.remote.model.card

data class ResponseCardYouStoreData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val id: Int,
        val title: String ,
        val name: String?,
        val createdAt: String,
        val isImage: Boolean,
    )
}