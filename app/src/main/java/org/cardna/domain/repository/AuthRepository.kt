package org.cardna.domain.repository

import org.cardna.data.remote.model.auth.RequestSignUpData
import org.cardna.data.remote.model.auth.ResponseSignUpData
import org.cardna.data.remote.model.auth.ResponseSocialLoginData

interface AuthRepository {
    suspend fun getKakaoLogin(): ResponseSocialLoginData

    suspend fun getNaverLogin(): ResponseSocialLoginData

    suspend fun postSignUp(requestSignUpData: RequestSignUpData): ResponseSignUpData
}