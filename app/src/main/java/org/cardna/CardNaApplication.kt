package org.cardna

import android.app.Activity
import android.app.Application
import com.google.firebase.FirebaseApp
//import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.util.PixelRatio
import timber.log.Timber

@HiltAndroidApp
class CardNaApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        initPixelUtil()
        initLogger()
        CardNaRepository.init(this)

//        initKakaoLogin()
        initKakaoLogin()
        CardNaRepository.init(this)
//        initFirebaseApp()
    }


    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(this)
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

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        isBackground = false
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll() //앱 실행시 모든 푸시알림 삭제
    }

    override fun onActivityResumed(p0: Activity) {
        isBackground = false
    }

    override fun onActivityPaused(p0: Activity) {
        isBackground = true
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    companion object {
        lateinit var pixelRatio: PixelRatio
        var isBackground = false
    }
}
