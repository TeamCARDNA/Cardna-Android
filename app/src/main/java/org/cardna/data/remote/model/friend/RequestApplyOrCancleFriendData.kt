package org.cardna.data.remote.model.friend

import com.squareup.moshi.Json

data class RequestApplyOrCancleFriendData(
    @Json(name = "friend") val friendId: Int
)