package org.cardna

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.cardna.presentation.util.PixelRatio
import timber.log.Timber

@HiltAndroidApp
class CardNaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initPixelUtil()
        initLogger()
        initKakaoLogin()
        initFirebaseApp()
    }

    private fun initPixelUtil() {
        pixelRatio = PixelRatio(this)
    }

    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKakaoLogin() {
        val kakaoAppKey = BuildConfig.KAKAO_APP_KEY
        KakaoSdk.init(this, kakaoAppKey)
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var pixelRatio: PixelRatio
    }
}
