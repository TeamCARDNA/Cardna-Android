package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.text.set
import androidx.core.text.toSpannable
import org.cardna.R
import org.cardna.databinding.ActivityOtherCardCreateCompleteBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.maincard.view.MainCardActivity
import org.cardna.presentation.util.LinearGradientSpan


class OtherCardCreateCompleteActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityOtherCardCreateCompleteBinding>(R.layout.activity_other_card_create_complete) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setTextGradient()
        setLottie(intent.getBooleanExtra(BaseViewUtil.IS_CARDPACK_OR_MAINCARD, BaseViewUtil.FROM_MAINCARD))
    }

    private fun setTextGradient() {
        val text = binding.tvOthercardcreateComplete.text.toString()
        val green = getColor(R.color.main_green)
        val purple = getColor(R.color.main_purple)
        val spannable = text.toSpannable()
        spannable[0..text.length] = LinearGradientSpan(text, text, green, purple)
        binding.tvOthercardcreateComplete.text = spannable
    }

    private fun setLottie(isCardPackOrMainCard: Boolean){
        val handler = Handler(Looper.getMainLooper())
        if(isCardPackOrMainCard){
            handler.postDelayed({
                var intent = Intent(this, FriendCardPackActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }, LOTTIE_VIEW_TIME)
        }
        else{
            if(intent.getBooleanExtra(BaseViewUtil.IS_CODE_OR_FRIEND, BaseViewUtil.FROM_FRIEND)){
                handler.postDelayed({
                    var intent = Intent(this, MainCardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }, LOTTIE_VIEW_TIME)

            }
            else{
                handler.postDelayed({
                    var intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }, LOTTIE_VIEW_TIME)
            }
        }
    }

    companion object {
        const val LOTTIE_VIEW_TIME = 1670L
    }
}