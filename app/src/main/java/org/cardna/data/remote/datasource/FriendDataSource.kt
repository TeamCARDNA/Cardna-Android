package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.friend.*
import retrofit2.http.Body
import retrofit2.http.POST

interface FriendDataSource {
    suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData

    suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData

    suspend fun postApplyOrCancleFriend(body: RequestApplyOrCancleFriendData): ResponseApplyOrCancleFriendData

    suspend fun postAcceptOrDenyFriend(body: RequestAcceptOrDenyFriendData): ResponseAcceptOrDenyFriendData
}