package org.cardna.domain.repository

import org.cardna.data.remote.model.friend.*

interface FriendRepository {
    suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData

    suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData

    suspend fun postApplyOrCancleFriend( body: RequestApplyOrCancleFriendData): ResponseApplyOrCancleFriendData

    suspend fun postAcceptOrDenyFriend(body: RequestAcceptOrDenyFriendData): ResponseAcceptOrDenyFriendData
}