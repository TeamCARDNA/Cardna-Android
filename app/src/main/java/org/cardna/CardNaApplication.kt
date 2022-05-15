package org.cardna

import android.app.Activity
import android.app.Application
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
    }


    //백&온그라운드 분기
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
