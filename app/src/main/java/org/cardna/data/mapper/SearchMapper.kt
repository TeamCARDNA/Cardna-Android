package org.cardna.data.mapper

import org.cardna.data.remote.model.friend.ResponseSearchFriendNameData
import org.cardna.data.remote.model.mypage.ResponseMyPageData

object SearchMapper {
    fun mapperToSearch(responseSearchFriendNameData: ResponseSearchFriendNameData): List<ResponseMyPageData.Data.FriendList> {
        return responseSearchFriendNameData.data.map {
            ResponseMyPageData.Data.FriendList(
                it.id,
                it.name,
                it.userImg,
                it.sentence ?: ""
            )
        }
    }
}
