package org.cardna.data.remote.model.friend

import com.google.gson.annotations.SerializedName

data class RequestApplyOrCancleFriendData(
    @SerializedName("friendId")  val friendId: Int
)