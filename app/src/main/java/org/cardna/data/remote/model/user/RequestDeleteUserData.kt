package org.cardna.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class RequestDeleteUserData(
    @SerializedName("reason") val reason: MutableList<Int>,
    @SerializedName("etc") val etc: String
)
