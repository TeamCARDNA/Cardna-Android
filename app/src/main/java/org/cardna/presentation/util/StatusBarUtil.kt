package org.cardna.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Window
import androidx.annotation.ColorInt

object StatusBarUtil {
    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    fun setStatusBar(activity: Activity, color: Int) {
        activity.window?.statusBarColor = color
    }
}