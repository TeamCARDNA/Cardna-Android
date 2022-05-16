package org.cardna

import android.app.Activity
import android.app.Application
import com.google.firebase.FirebaseApp
//import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
        initKakaoLogin()
        CardNaRepository.init(this)
        CardNaRepository.init(this)
        getDeviceToken()
    }

    //todo 소셜로그인에 필요한 디바이스 토큰을 얻는다
    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            //todo 여기서 사용자 토큰 저장
            CardNaRepository.fireBaseToken = token.toString()
            Timber.d("fcm token ${CardNaRepository.fireBaseToken}")
        })
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
        val kakaoAppKey = BuildConfig.KAKAO_NATIVE_KEY
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