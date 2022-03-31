package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.friend.ResponseFriendNameData

interface FriendDataSource {
    suspend fun getFriendName(name: String): ResponseFriendNameData
}