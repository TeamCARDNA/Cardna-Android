package org.cardna.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class RequestDeleteUserData(
    @SerializedName("reasons") val reasons: MutableList<Int>,
    @SerializedName("etc") val etc: String
)
