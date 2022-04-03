package org.cardna.domain.repository

import org.cardna.data.remote.model.friend.*
import org.cardna.data.remote.model.mypage.ResponseMyPageData

interface FriendRepository {
    suspend fun getSearchFriendName(name: String): List<ResponseMyPageData.Data.FriendList>

    suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData

    suspend fun postApplyOrCancleFriend( body: RequestApplyOrCancleFriendData): ResponseApplyOrCancleFriendData

    suspend fun postAcceptOrDenyFriend(body: RequestAcceptOrDenyFriendData): ResponseAcceptOrDenyFriendData
}