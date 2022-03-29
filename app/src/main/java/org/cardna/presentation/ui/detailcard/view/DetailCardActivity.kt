package org.cardna.presentation.ui.detailcard.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.IdRes
import com.example.cardna.R
import com.example.cardna.databinding.ActivityDetailCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.viewmodel.DetailCardViewModel
import org.cardna.presentation.util.setSrcWithGlide
import org.cardna.presentation.util.showCenterDialog
import org.cardna.presentation.util.showLottie

@AndroidEntryPoint
class DetailCardActivity : BaseViewUtil.BaseAppCompatActivity<ActivityDetailCardBinding>(R.layout.activity_detail_card) {
    private val detailCardViewModel: DetailCardViewModel by viewModels()
    private var cardType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.detailCardViewModel = detailCardViewModel
        binding.detailCardActivity = this
        initView()
    }

    override fun initView() {
        initData()  //TODO API완성 후 다시 test
        setObserve()
    }

    private fun initData() {
        val id = intent.getIntExtra(BaseViewUtil.CARD_ID, 0)
        detailCardViewModel.getDetailCard(id)  //TODO API완성 후 다시 test
    }

    @SuppressLint("ResourceType")
    private fun setObserve() {
        detailCardViewModel.detailCard.observe(this) { detailCard ->
            // cardType = detailCard.type   //TODO API완성 후 다시 test
            cardType = CARD_YOU
            setSrcWithGlide(detailCard.cardImg, binding.ivDetailcardImage)

            with(binding) {
                when (cardType) {
                    CARD_ME -> {
                        ctlDetailcardFriend.visibility = View.GONE
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_maingreen_stroke_real_black_2dp)
                        ibtnDetailcardEdit.setImageResource(R.drawable.ic_detail_card_me_trash)
                        showEditDialog(R.layout.dialog_detail_cardme)
                    }
                    CARD_YOU -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_mainpurple_stroke_real_black_2dp)
                        showEditDialog(R.layout.dialog_detail_cardyou)
                    }
                    STORAGE -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_white_1_5_stroke_real_black_2dp)
                        showEditDialog(R.layout.dialog_detail_storage)
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun showEditDialog(@IdRes layout: Int) {
        binding.ibtnDetailcardEdit.setOnClickListener {
            val dialog = this.showCenterDialog(layout)
            val deleteButton = dialog.findViewById<Button>(R.id.tv_dialog_delete)

            when (cardType) {
                CARD_ME -> {
                    val noButton = dialog.findViewById<Button>(R.id.tv_dialog_cardme_no)
                    noButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                CARD_YOU -> {
                    val saveButton = dialog.findViewById<Button>(R.id.tv_dialog_cardyou_save)
                    saveButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                STORAGE -> {
                    val declarationButton = dialog.findViewById<Button>(R.id.tv_dialog_storage_declaration)
                    declarationButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
            deleteButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun setShareClickListener() {
        startActivity(Intent(this@DetailCardActivity, CardShareActivity::class.java))
    }

    fun setLikeClickListener() {
        with(binding) {
            ctvDetailcardLike.toggle()
            detailCardViewModel?.postLike() ?: return

            if (ctvDetailcardLike.isChecked) {
                showLikeLottie()
                tvDetailcardLikecount.text = (++detailCardViewModel!!.likeCount).toString()
            } else {
                tvDetailcardLikecount.text = (--detailCardViewModel!!.likeCount).toString()
            }
        }
    }

    private fun showLikeLottie() {
        with(binding) {
            when (cardType) {
                CARD_ME -> showLottie(laDetailcardLottie, CARD_ME, "lottie_cardme.json")
                CARD_YOU -> showLottie(laDetailcardLottie, CARD_YOU, "lottie_cardyou.json")
            }
        }
    }

    companion object {
        const val CARD_ME = "me"
        const val CARD_YOU = "you"
        const val STORAGE = "storage"
    }
}