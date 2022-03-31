package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData

interface FriendDataSource {
    suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData

    suspend fun getSearchFriendCode(code: String ): ResponseSearchFriendCodeData
}