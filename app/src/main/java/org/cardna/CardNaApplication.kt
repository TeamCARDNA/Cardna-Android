package org.cardna

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.cardna.presentation.util.PixelRatio
import timber.log.Timber

@HiltAndroidApp
class CardNaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initPixelUtil()
        initLogger()
    }

    private fun initPixelUtil() {
        pixelRatio = PixelRatio(this)
    }

    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var pixelRatio: PixelRatio
    }
}
