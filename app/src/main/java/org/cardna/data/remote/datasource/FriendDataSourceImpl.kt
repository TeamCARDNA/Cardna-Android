package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.friend.FriendService
import org.cardna.data.remote.model.friend.*
import javax.inject.Inject

class FriendDataSourceImpl @Inject constructor(
    private val friendService: FriendService
) : FriendDataSource {

    override suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData {
        return friendService.getSearchFriendName(name)
    }

    override suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData {
        return friendService.getSearchFriendCode(code)
    }

    override suspend fun postApplyOrCancleFriend(body: RequestApplyOrCancleFriendData): ResponseApplyOrCancleFriendData {
        return friendService.postApplyOrCancleFriend(body)
    }

    override suspend fun postAcceptOrDenyFriend(body: RequestAcceptOrDenyFriendData): ResponseAcceptOrDenyFriendData {
        return friendService.postAcceptOrDenyFriend(body)
    }
}