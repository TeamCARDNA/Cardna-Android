package org.cardna.data.remote.api.friend

import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {

    @GET("friend/list?name")
    suspend fun getSearchFriendName(
        @Query("name") name: String
    ): ResponseSearchFriendNameData

    @GET("friend/search?code")
    suspend fun getSearchFriendCode(
        @Query("code") code: String
    ): ResponseSearchFriendCodeData
}