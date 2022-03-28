package org.cardna.presentation.ui.login

import android.os.Bundle
import android.text.Spannable
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySetNameFinishedBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.LinearGradientSpan

class SetNameFinishedActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySetNameFinishedBinding>(R.layout.activity_set_name_finished) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setSleep()
        setClickListener()
        setTextView()
    }

    private fun setClickListener() {
        negativeButtonClickListener()
        positiveButtonClickListener()
    }

    private fun setTextView() {
        val welcomeText = intent.getStringExtra("welcomeText")

        with(binding) {
            tvSetnamefinishedTitle.text = setGradientText(welcomeText!!)
            tvSetnamefinishedMessage1.text =
                setGradientText(getString(R.string.setnamefinished_tv_message1))
            tvSetnamefinishedMessage2.text =
                setGradientText(getString(R.string.setnamefinished_tv_message2))
            tvSetnamefinishedMessage3.text =
                setGradientText(getString(R.string.setnamefinished_tv_message3))
        }
    }

    private fun setGradientText(welcomeText: String): Spannable {
        val green = getColor(R.color.main_green)
        val purple = getColor(R.color.main_purple)
        val spannable = welcomeText.toSpannable()
        spannable[0..welcomeText.length] =
            LinearGradientSpan(welcomeText, welcomeText, green, purple)
        return spannable
    }

    private fun setSleep() {

    }

    private fun negativeButtonClickListener() {
        //EmptyView -> 메인 페이지 이동
    }

    private fun positiveButtonClickListener() {
        //카드나 작성뷰로 이동
    }
}