package org.cardna.presentation.ui.detailcard.view

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.example.cardna.R
import com.example.cardna.databinding.ActivityDetailCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.viewmodel.DetailCardViewModel
import org.cardna.presentation.util.setSrcWithGlide

@AndroidEntryPoint
class DetailCardActivity : BaseViewUtil.BaseAppCompatActivity<ActivityDetailCardBinding>(R.layout.activity_detail_card) {
    private val detailCardViewModel: DetailCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.detailCardViewModel = detailCardViewModel

        initView()
    }

    override fun initView() {
        initData()  //TODO API완성 후 다시 test
        setObserve()
        setLikeClickListener()
    }

    private fun initData() {
        val id = intent.getIntExtra(BaseViewUtil.CARD_ID, 0)
        detailCardViewModel.getDetailCard(277)
    }

    private fun setObserve() {
        detailCardViewModel.detailCard.observe(this) { detailCard ->
            setSrcWithGlide(detailCard.cardImg!!, binding.ivDetailcardImage)
        }

        detailCardViewModel.type.observe(this) { type ->
            with(binding) {
                when (type) {
                    CARD_ME -> {
                        ctlDetailcardFriend.visibility = View.GONE
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_maingreen_stroke_real_black_2dp)
                        ibtnDetailcardRight.setImageResource(R.drawable.ic_detail_card_me_trash)
                    }
                    CARD_YOU -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_mainpurple_stroke_real_black_2dp)
                    }
                    STORAGE -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_white_1_5_stroke_real_black_2dp)
                    }
                }
            }
        }
    }

    private fun setLikeClickListener() {
        with(binding) {
            ctvDetailcardLike.setOnClickListener {
                ctvDetailcardLike.toggle()
                if (ctvDetailcardLike.isChecked) {
                    showLikeLottie()
                    tvDetailcardLikecount.text = (++detailCardViewModel!!.likeCount).toString()
                } else if (!ctvDetailcardLike.isChecked) {
                    tvDetailcardLikecount.text = (--detailCardViewModel!!.likeCount).toString()
                }
            }
        }
    }

    private fun showLikeLottie() {
        val lottie = findViewById<LottieAnimationView>(R.id.la_detailcard_lottie)

        detailCardViewModel.type.observe(this) { type ->
            when (CARD_YOU) {   //TODO 서버 API 완성 시 type으로 변환환
               CARD_ME -> {
                    lottie.setAnimation("lottie_cardme.json")
                }
                CARD_YOU -> {
                    lottie.setAnimation("lottie_cardyou.json")
                }
            }
        }

        binding.laDetailcardLottie.visibility = View.VISIBLE
        lottie.loop(false)
        lottie.playAnimation()
        lottie.repeatCount = 1

        lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                lottie.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                lottie.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        lottie.visibility = View.GONE
    }


    companion object {
        const val CARD_ME = "me"
        const val CARD_YOU = "you"
        const val STORAGE = "storage"
    }
}