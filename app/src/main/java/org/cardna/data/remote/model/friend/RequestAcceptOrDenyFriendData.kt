package org.cardna.data.remote.model.friend

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class RequestAcceptOrDenyFriendData(
    @Json(name = "friendId")  val friendId: Int,
    @Json(name = "isAccept")  val isAccept: Boolean
)