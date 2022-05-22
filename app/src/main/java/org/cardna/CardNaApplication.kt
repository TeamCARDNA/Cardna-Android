package org.cardna

//import com.google.firebase.FirebaseApp
import android.R.id
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.amplitude.api.Amplitude
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.log.NidLog
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
        initAmplitude()
        initNaverLogin()
        CardNaRepository.init(this)
        getDeviceToken()
   //     Amplitude.getInstance().logEvent("APP OPEN")
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

    private fun initNaverLogin(){
        NidLog.init()
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_API_CLIENT_ID,
            BuildConfig.NAVER_API_CLIENT_SECRET,
            BuildConfig.NAVER_API_APP_NAME
        )
    }

    private fun initAmplitude() {
        Amplitude.getInstance().initialize(this, "00c76df54b75da7bd287245491b78c37").enableForegroundTracking(this)
    }

    //백&온그라운드 분기
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        isBackground = false
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