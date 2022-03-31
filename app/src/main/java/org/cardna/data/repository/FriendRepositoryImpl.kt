package org.cardna.data.repository

import org.cardna.data.remote.datasource.FriendDataSource
import org.cardna.data.remote.model.friend.ResponseFriendNameData
import org.cardna.domain.repository.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDataSource: FriendDataSource
) : FriendRepository {

    override suspend fun getFriendName(name: String): ResponseFriendNameData {
        return friendDataSource.getFriendName(name)
    }
}