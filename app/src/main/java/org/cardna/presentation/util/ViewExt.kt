package org.cardna.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Spannable
import android.view.*
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.kakao.sdk.common.model.AuthErrorCause
import org.cardna.R
import org.cardna.CardNaApplication
import org.cardna.presentation.ui.editcard.adapter.EditCardAdapter
import timber.log.Timber
import kotlin.math.roundToInt


fun AppCompatActivity.add(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .add(frameId, fragment, null)
        .commit()
}

fun AppCompatActivity.add(@IdRes frameId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager
        .beginTransaction()
        .add(frameId, fragment, tag)
        .commit()
}

fun AppCompatActivity.replace(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(frameId, fragment, null)
        .commit()
}

fun AppCompatActivity.replace(@IdRes frameId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager
        .beginTransaction()
        .replace(frameId, fragment, tag)
        .commit()
}

fun AppCompatActivity.show(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .show(fragment)
        .commit()
}

fun AppCompatActivity.hide(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .hide(fragment)
        .commit()
}


fun Context.convertDPtoPX(dp: Int): Int {
    val density: Float = this.resources.displayMetrics.density
    return (dp.toFloat() * density).roundToInt()
}

@SuppressLint("ResourceType")
fun Context.showCustomDialog(@IdRes layout: Int): Dialog {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(layout)
    dialog.setContentView(layout)
    dialog.setCancelable(true)
    dialog.getWindow()!!.setGravity(Gravity.CENTER)
    dialog.show()
    return dialog
}

fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getToast(msg: String): Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)

fun Activity.setSrcWithGlide(imageUrl: String, imageView: ImageView) {
    Glide
        .with(this)
        .load(imageUrl)
        .into(imageView)
}

fun SearchView.setTextColor(context: Context, hintColorInt: Int, textColorInt: Int) {
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setHintTextColor(
        context.getColor(
            hintColorInt
        )
    )
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setTextColor(
        context.getColor(
            textColorInt
        )
    )
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


fun Context.showCustomPopUp(
    view: ImageButton,
    arrayInt: Int,
    baseContext: Context
): ListPopupWindow {

    val items = this.resources.getStringArray(arrayInt)
    val popupAdapter =
        object : ArrayAdapter<String>(baseContext, R.layout.item_detail, items) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getView(position, convertView, parent)
                val color = if (position == 0) {
                    R.color.dark_gray
                } else {
                    R.color.dark_gray
                }
                (view as TextView).setTextColor(ContextCompat.getColor(context, color))
                return view
            }
        }

    val popup = ListPopupWindow(this).apply {
        anchorView = view
        setAdapter(popupAdapter)
        setDropDownGravity(Gravity.NO_GRAVITY)
        width = measureContentWidth(baseContext, popupAdapter)
        height = ListPopupWindow.WRAP_CONTENT
        isModal = true
        val screenWidth = CardNaApplication.pixelRatio.screenWidth / 5
        val screenWidthStr = "-$screenWidth"
        horizontalOffset = screenWidthStr.toInt()
        verticalOffset = -20

        setBackgroundDrawable(ContextCompat.getDrawable(baseContext, R.drawable.pop_up))
    }
    return popup
}

fun Context.setGradientText(
    inputText: String,
): Spannable {
    val green = getColor(R.color.main_green)
    val purple = getColor(R.color.main_purple)
    val spannable = inputText.toSpannable()
    spannable[0..inputText.length] =
        LinearGradientSpan(inputText, inputText, green, purple)
    return spannable
}

fun Context.setBeforeGradientText(
    inputText: String,
    color: Int
): Spannable {
    val spannable = inputText.toSpannable()
    spannable[0..inputText.length] = LinearGradientSpan(inputText, inputText, color, color)
    return spannable
}

fun Context.setSpannableColor(inputText: String, inputColor: Int, start: Int, end: Int): Spannable {
    val color = getColor(inputColor)
    val spannable = inputText.toSpannable()
    spannable[start..end] =
        LinearGradientSpan(inputText, inputText, color, color)
    return spannable
}

fun Context.getPageTransformer(): ViewPager2.PageTransformer {
    val compositePageTransformer = CompositePageTransformer()
    compositePageTransformer.addTransformer(MarginPageTransformer((20 * resources.displayMetrics.density).roundToInt()))

    return compositePageTransformer
}

fun Context.viewPagerAnimation(viewpager: ViewPager2) {
    val compositePageTransformer = getPageTransformer()
    with(viewpager) {
        clipToPadding = false
        clipChildren = false
        offscreenPageLimit = 1
        setPageTransformer(compositePageTransformer)
        setPadding(
            (56 * resources.displayMetrics.density).roundToInt(),
            0,
            (56 * resources.displayMetrics.density).roundToInt(),
            0
        )
        getChildAt(0).overScrollMode = androidx.recyclerview.widget.RecyclerView.OVER_SCROLL_NEVER
    }
}

fun copyText(context: Context, text: String) {
    val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip: ClipData = ClipData.newPlainText("Label", text)
    myClipboard.setPrimaryClip(myClip)
}

fun itemTouchHelperListener(
    editCardAdapter: EditCardAdapter,
    recyclerView: RecyclerView
) {
    val itemTouchHelperCallback = ItemTouchHelperCallback(editCardAdapter)
    val helper = ItemTouchHelper(itemTouchHelperCallback)
    helper.attachToRecyclerView(recyclerView)
}


fun getErrorLog(error: Throwable) {
    when {
        error.toString() == AuthErrorCause.AccessDenied.toString() -> {
            Timber.e("접근이 거부 됨(동의 취소)")
        }
        error.toString() == AuthErrorCause.InvalidClient.toString() -> {
            Timber.e("유효하지 않은 앱")
        }
        error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
            Timber.e("인증 수단이 유효하지 않아 인증할 수 없는 상태")
        }
        error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
            Timber.e("요청 파라미터 오류")
        }
        error.toString() == AuthErrorCause.InvalidScope.toString() -> {
            Timber.e("유효하지 않은 scope ID")
        }
        error.toString() == AuthErrorCause.Misconfigured.toString() -> {
            Timber.e("설정이 올바르지 않음(android key hash)")
        }
        error.toString() == AuthErrorCause.ServerError.toString() -> {
            Timber.e("서버 내부 에러")
        }
        error.toString() == AuthErrorCause.Unauthorized.toString() -> {
            Timber.e("앱이 요청 권한이 없음")
        }
        else -> { // Unknown
            Timber.e("기타 에러")
        }
    }
}