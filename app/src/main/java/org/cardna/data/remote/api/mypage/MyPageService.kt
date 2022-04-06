package org.cardna.data.remote.api.mypage

import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.mypage.ResponseMyPageUserData
import retrofit2.http.GET

interface MyPageService {

    @GET("my-page")
    suspend fun getMyPage(): ResponseMyPageData

    @GET("my-page/user")
    suspend fun getMyPageUser(): ResponseMyPageUserData
}