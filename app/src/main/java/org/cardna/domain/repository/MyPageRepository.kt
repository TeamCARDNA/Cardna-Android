package org.cardna.domain.repository

import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.mypage.ResponseMyPageUserData

interface MyPageRepository {

    suspend fun getMyPage(): ResponseMyPageData

    suspend fun getMyPageUser(): ResponseMyPageUserData
}