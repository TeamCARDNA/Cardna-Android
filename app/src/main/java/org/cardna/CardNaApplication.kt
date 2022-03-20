package org.cardna

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CardNaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
