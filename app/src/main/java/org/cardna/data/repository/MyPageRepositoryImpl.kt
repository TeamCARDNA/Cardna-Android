package org.cardna.data.repository

import org.cardna.data.remote.datasource.MyPageDataSource
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.mypage.ResponseMyPageUserData
import org.cardna.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource
) : MyPageRepository {

    override suspend fun getMyPage(): ResponseMyPageData {
        return myPageDataSource.getMyPage()
    }

    override suspend fun getMyPageUser(): ResponseMyPageUserData {
        return myPageDataSource.getMyPageUser()
    }
}