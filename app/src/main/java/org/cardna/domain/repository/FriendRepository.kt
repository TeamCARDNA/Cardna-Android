package org.cardna.domain.repository

import org.cardna.data.remote.model.friend.ResponseFriendNameData

interface FriendRepository {
    suspend fun getFriendName(name: String): ResponseFriendNameData
}