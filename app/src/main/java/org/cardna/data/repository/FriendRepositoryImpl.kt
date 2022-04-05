package org.cardna.data.repository

import org.cardna.data.mapper.SearchMapper
import org.cardna.data.remote.datasource.FriendDataSource
import org.cardna.data.remote.model.friend.*
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.domain.repository.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDataSource: FriendDataSource
) : FriendRepository {

/*    override suspend fun getSearchFriendName(name: String): ResponseSearchFriendNameData {
        return friendDataSource.getSearchFriendName(name)
    }*/

   override suspend fun getSearchFriendName(name: String): List<ResponseMyPageData.Data.FriendList> {
        return SearchMapper.mapperToSearch(friendDataSource.getSearchFriendName(name))
    }

    override suspend fun getSearchFriendCode(code: String): ResponseSearchFriendCodeData {
        return friendDataSource.getSearchFriendCode(code)
    }

    override suspend fun postApplyOrCancleFriend(body: RequestApplyOrCancleFriendData): ResponseApplyOrCancleFriendData {
        return friendDataSource.postApplyOrCancleFriend(body)
    }

    override suspend fun postAcceptOrDenyFriend(body: RequestAcceptOrDenyFriendData): ResponseAcceptOrDenyFriendData {
        return friendDataSource.postAcceptOrDenyFriend(body)
    }
}