package org.cardna.data.remote.api.friend

import org.cardna.data.remote.model.friend.*
import org.cardna.data.remote.model.like.RequestLikeData
import org.cardna.data.remote.model.like.ResponseLikeData
import org.cardna.data.remote.model.user.RequestDeleteUserData
import org.cardna.data.remote.model.user.ResponseDeleteUserData
import retrofit2.http.*

interface FriendService {

    @GET("friend/list?name")
    suspend fun getSearchFriendName(
        @Query("name") name: String
    ): ResponseSearchFriendNameData

    @GET("friend/search?code")
    suspend fun getSearchFriendCode(
        @Query("code") code: String
    ): ResponseSearchFriendCodeData

    @POST("friend/request")
    suspend fun postApplyOrCancleFriend(
        @Body body: RequestApplyOrCancleFriendData
    ): ResponseApplyOrCancleFriendData

    @POST("friend/response")
    suspend fun postAcceptOrDenyFriend(
        @Body body: RequestAcceptOrDenyFriendData
    ): ResponseAcceptOrDenyFriendData
}