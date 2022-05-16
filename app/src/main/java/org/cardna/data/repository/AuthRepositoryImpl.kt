package org.cardna.data.repository

import org.cardna.data.remote.datasource.AuthDataSource
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.data.remote.model.auth.ResponseTokenIssuanceData
import org.cardna.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun getKakaoLogin(): ResponseSocialLoginData {
        return authDataSource.getKakaoLogin()
    }

    override suspend fun getNaverLogin(): ResponseSocialLoginData {
        return authDataSource.getNaverLogin()
    }

    override suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData {
        return authDataSource.postSignUp(requestSignUpData)
    }

    override suspend fun getTokenIssuance(accessToken: String, refreshToken: String): ResponseTokenIssuanceData {
        return authDataSource.getTokenIssuance(accessToken, refreshToken)
    }
}