package org.cardna.data.remote.model.like

import com.google.gson.annotations.SerializedName

data class RequestLikeData(
    @SerializedName("cardId")  val cardId: Int
)