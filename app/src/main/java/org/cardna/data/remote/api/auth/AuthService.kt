package org.cardna.data.remote.api.auth

import org.cardna.data.remote.model.auth.ResponseSocialLogin
import retrofit2.http.GET

interface AuthService {
    @GET("auth/kakao")
    suspend fun getKakaoLogin(): ResponseSocialLogin

    @GET("auth/naver")
    suspend fun getNaverLogin(): ResponseSocialLogin
}