package org.cardna.data.remote.datasource

import org.cardna.data.remote.model.auth.ResponseSocialLogin

interface AuthDataSource {

    suspend fun getKakaoLogin(): ResponseSocialLogin

    suspend fun getNaverLogin(): ResponseSocialLogin
}