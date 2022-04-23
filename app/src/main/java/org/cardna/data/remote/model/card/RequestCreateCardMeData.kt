package org.cardna.data.remote.model.card

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class RequestCreateCardMeData(
    val title: String,
    val content: String,
    val symbolId: Int ?
) {
    fun toRequestBody() : HashMap<String, RequestBody> { // STRING 부분이 KEY 값,
        return hashMapOf(
            "title" to title.toRequestBody("text/plain".toMediaTypeOrNull()),
            "content" to content.toRequestBody("text/plain".toMediaTypeOrNull()),
            "symbolId" to symbolId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        )
    }
}