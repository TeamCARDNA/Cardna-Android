package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.card.CardService
import org.cardna.data.remote.api.friend.FriendService
import org.cardna.data.remote.model.card.ResponseDetailCardData
import org.cardna.data.remote.model.friend.ResponseFriendNameData
import javax.inject.Inject

class FriendDataSourceImpl @Inject constructor(
    private val friendService: FriendService
) : FriendDataSource {

    override suspend fun getFriendName(name: String): ResponseFriendNameData {
        return friendService.getFriendName(name)
    }
}