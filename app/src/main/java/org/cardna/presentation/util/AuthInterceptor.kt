package org.cardna.presentation.util

import okhttp3.Interceptor
import okhttp3.Response
import org.cardna.data.local.singleton.CardNaRepository
import timber.log.Timber

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        //카카오로 소셜로그인시 인터셉트
        //네이버로 소셜로그인시 인터셉트
        //현재 유저 토큰 인터셉트
        with(CardNaRepository) {
            if (kakaoAccessToken.isNotEmpty()) {
                kakaoAccessToken.let {
                    requestBuilder.addHeader("token", it)
                }
                Timber.e("ㅡㅡㅡㅡㅡㅡㅡ1.kakaoAccessTokenㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${kakaoAccessToken}")
            } else if (naverAccessToken.isNotEmpty()) {
                naverAccessToken.let {
                    requestBuilder.addHeader("token", it)
                }
                Timber.e("ㅡㅡㅡㅡㅡㅡㅡ2.naverAccessTokenㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${naverAccessToken}")
            } else if (userToken.isNotEmpty()) {
                userToken.let {
                    requestBuilder.addHeader("token", it)
                }
                Timber.e("ㅡㅡㅡㅡㅡㅡㅡ3.userTokenㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${userToken}")
                return chain.proceed(requestBuilder.build())
            } else null
        }
        return chain.proceed(requestBuilder.build())
    }
}