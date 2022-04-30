package org.cardna.data.repository

import org.cardna.data.remote.datasource.AuthDataSource
import org.cardna.data.remote.model.auth.ResponseSocialLogin
import org.cardna.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRepository: AuthDataSource
) : AuthRepository {
    override suspend fun getKakaoLogin(): ResponseSocialLogin {
        return authRepository.getKakaoLogin()
    }

    override suspend fun getNaverLogin(): ResponseSocialLogin {
        return authRepository.getNaverLogin()
    }
}