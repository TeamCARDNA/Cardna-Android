package org.cardna.data.remote.api.auth

import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData
import retrofit2.http.*

interface AuthService {

    @GET("auth/kakao")
    suspend fun getKakaoLogin(
        @Header("fcmtoken") fcmToken: String = CardNaRepository.fireBaseToken
    ): ResponseSocialLoginData

    // 소셜 로그인 API - 네이버
    @GET("auth/naver")
    suspend fun getNaverLogin(
        @Header("fcmtoken") fcmToken: String = CardNaRepository.fireBaseToken
    ): ResponseSocialLoginData

    // 이름 등록 및 회원가입 API
    @POST("auth")
    suspend fun postSignUp(
        @Body body: RequestSignUpData
    ): ResponseSignUpData


    // 토큰 재발급 API
    @GET("auth/token")
    suspend fun getTokenIssuance(
        @Header("accessToken") accessToken: String,
        @Header("refreshToken") refreshToken: String,
    ): ResponseTokenIssuanceData
}