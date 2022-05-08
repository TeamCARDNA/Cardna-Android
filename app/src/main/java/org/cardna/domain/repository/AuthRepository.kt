package org.cardna.domain.repository

import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData

interface AuthRepository {
    suspend fun getKakaoLogin(): ResponseSocialLoginData

    suspend fun getNaverLogin(fcmToken: String): ResponseSocialLoginData

    suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData

    suspend fun getTokenIssuance(accessToken: String, refreshToken: String): ResponseTokenIssuanceData
}