package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.auth.AuthService
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {

    override suspend fun getKakaoLogin(): ResponseSocialLoginData {
        return authService.getKakaoLogin()
    }

    override suspend fun getNaverLogin(): ResponseSocialLoginData {
        return authService.getNaverLogin()
    }

    override suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData {
        return authService.postSignUp(requestSignUpData)
    }
}