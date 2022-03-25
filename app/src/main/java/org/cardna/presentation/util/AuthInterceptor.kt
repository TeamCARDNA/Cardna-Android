package org.cardna.presentation.util

import okhttp3.Interceptor
import okhttp3.Response
import org.cardna.data.local.singleton.CardNaRepository

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        CardNaRepository.userToken.let {
            requestBuilder.addHeader("token", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}