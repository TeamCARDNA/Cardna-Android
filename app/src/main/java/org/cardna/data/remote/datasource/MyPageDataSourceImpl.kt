package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.mypage.MyPageService
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.mypage.ResponseMyPageUserData
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageService: MyPageService
) : MyPageDataSource {

    override suspend fun getMyPage(): ResponseMyPageData {
        return myPageService.getMyPage()
    }

    override suspend fun getMyPageUser(): ResponseMyPageUserData {
        return myPageService.getMyPageUser()
    }
}