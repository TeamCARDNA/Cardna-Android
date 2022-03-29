package org.cardna.presentation.util

import android.animation.Animator
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity


fun showLottie(view: LottieAnimationView, type: String, json: String) {
    val lottie = view

    when (type) {   //TODO 서버 API 완성 시 type으로 변환환
        DetailCardActivity.CARD_ME -> {
            lottie.setAnimation(json)
        }
        DetailCardActivity.CARD_YOU -> {
            lottie.setAnimation(json)
        }
    }

    view.visibility = View.VISIBLE
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