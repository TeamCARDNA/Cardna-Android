package org.cardna.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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

@SuppressLint("ResourceType")
fun Context.showCustomDialog(@IdRes layout: Int, paramsX: Int? = null, paramsY: Int? = null): Dialog {
    val dialog = Dialog(this)
    val params = dialog.window?.attributes
/*
    if (paramsX != null && paramsY != null) {
        params?.y = paramsY
        params?.x = paramsX
        dialog.window?.attributes = params
    }*/

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(layout)
    dialog.setContentView(layout)
    dialog.setCancelable(false)
    dialog.getWindow()!!.setGravity(Gravity.CENTER)
    dialog.show()
    return dialog
}

fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.setSrcWithGlide(imageUrl: String, imageView: ImageView) {
    Glide
        .with(this)
        .load(imageUrl)
        .into(imageView)
}

fun SearchView.setTextColor(context: Context, hintColorInt: Int, textColorInt: Int) {
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setHintTextColor(context.getColor(hintColorInt))
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setTextColor(context.getColor(textColorInt))
}

fun SearchView.setTextSize(size: Float) {
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).textSize = size
}

fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}