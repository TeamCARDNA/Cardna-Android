package org.cardna.data.remote.datasource

import org.cardna.data.remote.api.auth.AuthService
import org.cardna.data.remote.model.auth.ResponseSocialLogin
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {

    override suspend fun getKakaoLogin(): ResponseSocialLogin {
        return authService.getKakaoLogin()
    }

    override suspend fun getNaverLogin(): ResponseSocialLogin {
        return authService.getNaverLogin()
    }
}