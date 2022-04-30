package org.cardna.domain.repository

import org.cardna.data.remote.model.auth.ResponseSocialLogin

interface AuthRepository {
    suspend fun getKakaoLogin(): ResponseSocialLogin

    suspend fun getNaverLogin(): ResponseSocialLogin
}