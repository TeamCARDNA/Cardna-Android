package org.cardna.data.repository

import org.cardna.data.remote.datasource.AuthDataSource
import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData
import org.cardna.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRepository: AuthDataSource
) : AuthRepository {
    override suspend fun getKakaoLogin(): ResponseSocialLoginData {
        return authRepository.getKakaoLogin()
    }

    override suspend fun getNaverLogin(): ResponseSocialLoginData {
        return authRepository.getNaverLogin()
    }

    override suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData {
        return authRepository.postSignUp(requestSignUpData)
    }
}