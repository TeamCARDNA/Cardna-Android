package org.cardna.data.remote.model.like

import com.squareup.moshi.Json

data class RequestLikeData(
    @Json(name = "cardId") val cardId: Int
)