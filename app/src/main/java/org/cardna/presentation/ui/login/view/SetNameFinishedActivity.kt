package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.CardNaApplication
import org.cardna.R
import org.cardna.databinding.ActivitySetNameFinishedBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.view.CardCreateActivity

import org.cardna.presentation.util.LinearGradientSpan

import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.setGradientText

@AndroidEntryPoint
class SetNameFinishedActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySetNameFinishedBinding>(R.layout.activity_set_name_finished) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
//        getScreenHeight()
        setClickListener()
        setUpAnim(R.anim.anim_translate_up_2560height,1300L)
        setTextView()
    }

//    private fun getScreenHeight() {
//        val screenHeight = CardNaApplication.pixelRatio.screenHeight
//        if (screenHeight > 2872 || screenHeight < 2560) {
//            setUpAnim(R.anim.anim_translate_up_2560height, 1300L)
//        } else if (screenHeight in 2560..2872) {
//            setUpAnim(R.anim.anim_translate_up_2872height, 1300L)
//        }
//    }

    private fun setUpAnim(animId: Int, delayMillis: Long) {
        val animation = AnimationUtils.loadAnimation(this, animId)
        binding.ctlSetnamefinished.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({ setFadeAnim() }, delayMillis)
    }

    private fun setFadeAnim() {
        with(binding.ctlSetnamefinishedMessage) {
            visibility = View.VISIBLE
            val fadeIn = AnimationUtils.loadAnimation(this@SetNameFinishedActivity, R.anim.fade_in)
            val fadeOut =
                AnimationUtils.loadAnimation(this@SetNameFinishedActivity, R.anim.fade_out)
            startAnimation(fadeOut)
            startAnimation(fadeIn)
            Handler(Looper.getMainLooper()).postDelayed({ setButtonAnim() }, 670)
        }
    }

    private fun setButtonAnim() {
        with(binding.llSetnamefinishedBtn) {
            visibility = View.VISIBLE
            val fadeIn = AnimationUtils.loadAnimation(this@SetNameFinishedActivity, R.anim.fade_in)
            val fadeOut =
                AnimationUtils.loadAnimation(this@SetNameFinishedActivity, R.anim.fade_out)
            startAnimation(fadeOut)
            startAnimation(fadeIn)
        }
    }


    private fun setClickListener() {
        negativeButtonClickListener()
        positiveButtonClickListener()
    }

    private fun setTextView() {
        val welcomeText = intent.getStringExtra("welcomeText") ?: "반가워요"

        with(binding) {
            tvSetnamefinishedTitle.text = setGradientText(welcomeText)
            tvSetnamefinishedMessage1.text =
                setGradientText(getString(R.string.setnamefinished_tv_message1))
            tvSetnamefinishedMessage2.text =
                setGradientText(getString(R.string.setnamefinished_tv_message2))
            tvSetnamefinishedMessage3.text =
                setGradientText(getString(R.string.setnamefinished_tv_message3))
        }
    }

    private fun setNextActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun negativeButtonClickListener() {
        binding.btnSetnamefinishedNegative.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun positiveButtonClickListener() {
        binding.btnSetnamefinishedPositive.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(GO_TO_CARDCREAT_ACTIVITY_KEY, GO_TO_CARDCREAT_ACTIVITY)
            })
        }
    }

    companion object {
        const val GO_TO_CARDCREAT_ACTIVITY_KEY = "GO_TO_CARDCREAT_ACTIVITY_KEY"
        const val GO_TO_CARDCREAT_ACTIVITY = "GO_TO_CARDCREAT_ACTIVITY"
    }
}
