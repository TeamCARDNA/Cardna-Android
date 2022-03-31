package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.friend.FriendService
import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData
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
}