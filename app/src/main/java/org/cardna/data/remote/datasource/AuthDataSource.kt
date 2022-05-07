package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData

interface AuthDataSource {

    suspend fun getKakaoLogin(): ResponseSocialLoginData

    suspend fun getNaverLogin(fcmToken: String): ResponseSocialLoginData

    suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData

    suspend fun getTokenIssuance(accessToken: String, refreshToken: String): ResponseTokenIssuanceData
}