package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.mypage.ResponseMyPageUserData

interface MyPageDataSource {

    suspend fun getMyPage(): ResponseMyPageData

    suspend fun getMyPageUser(): ResponseMyPageUserData
}