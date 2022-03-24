package org.cardna.presentation.util

import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun AppCompatActivity.replace(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(frameId, fragment, null)
        .commit()
}

fun Context.convertDPtoPX(dp: Int): Int {
    val density: Float = this.resources.displayMetrics.density
    return (dp.toFloat() * density).roundToInt()
}
