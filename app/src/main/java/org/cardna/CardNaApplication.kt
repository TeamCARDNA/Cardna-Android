package org.cardna

import android.app.Application
import com.example.cardna.R
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
    }

    private fun initPixelUtil() {
        pixelRatio = PixelRatio(this)
    }

    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKakaoLogin() {
        KakaoSdk.init(this, getString(R.string.KAKAO_APP_KEY))
    }

    companion object {
        lateinit var pixelRatio: PixelRatio
    }
}
