package org.cardna.data.remote.api.user

import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.data.remote.model.user.*
import retrofit2.http.*

interface UserService {


    @HTTP(method = "DELETE", path = "user", hasBody = true)
    suspend fun deleteUser(
        @Body body: RequestDeleteUserData
    ): ResponseDeleteUserData

    @POST("user/report")
    suspend fun postReportUser(
        @Body body: RequestPostReportUserData
    ): ResponsePostReportUserData

    @GET("user")
    suspend fun getUser(): ResponseUserData
}