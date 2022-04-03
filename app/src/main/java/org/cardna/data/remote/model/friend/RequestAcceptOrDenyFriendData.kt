package org.cardna.data.remote.model.friend

import com.google.gson.annotations.SerializedName

data class RequestAcceptOrDenyFriendData(
    @SerializedName("friendId") val friendId: Int,
    @SerializedName("isAccept") val isAccept: Boolean
)