package org.cardna.domain.repository

import org.cardna.data.remote.model.friend.ResponseSearchFriendCodeData
import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData

interface FriendRepository {
    suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData

    suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData
}