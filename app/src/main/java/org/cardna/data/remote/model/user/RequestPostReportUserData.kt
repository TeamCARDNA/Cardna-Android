package org.cardna.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class RequestPostReportUserData(
    @SerializedName("userId") val userId: Int,
    @SerializedName("cardId") val cardId: Int,
    @SerializedName("reason") val reason: String
)