package org.cardna.data.remote.api.auth

import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData
import retrofit2.http.*

interface AuthService {

    @GET("auth/kakao")
    suspend fun getKakaoLogin(
        @Header("token") socialToken: String,
        @Header("fcmtoken") fcmToken: String,
    ): ResponseSocialLoginData

    @GET("auth/naver")
    suspend fun getNaverLogin(): ResponseSocialLoginData

    @POST("auth")
    suspend fun postSignUp(
        @Body body: RequestSignUpData
    ): ResponseSignUpData

    @GET("auth/token")
    suspend fun getTokenIssuance(
        @Header("accessToken") accessToken: String,
        @Header("refreshToken") refreshToken: String,
    ): ResponseTokenIssuanceData

}