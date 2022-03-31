package org.cardna.data.remote.api.friend

import org.cardna.data.remote.model.friend.ResponseFriendNameData
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {

    @GET("friend/list?name")
    suspend fun getFriendName(
        @Query("name") name: String
    ): ResponseFriendNameData
}