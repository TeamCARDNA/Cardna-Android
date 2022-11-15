package org.cardna

//import com.google.firebase.FirebaseApp
import android.app.Application
import com.amplitude.api.Amplitude
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.log.NidLog
import dagger.hilt.android.HiltAndroidApp
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.util.PixelRatio
import timber.log.Timber


@HiltAndroidApp
class CardNaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initPixelUtil()
        initLogger()
        initKakaoLogin()
        initAmplitude()
        initNaverLogin()
        CardNaRepository.init(this)
        getDeviceToken()
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            CardNaRepository.fireBaseToken = token.toString()
        })
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

    companion object {
        lateinit var pixelRatio: PixelRatio
    }
}