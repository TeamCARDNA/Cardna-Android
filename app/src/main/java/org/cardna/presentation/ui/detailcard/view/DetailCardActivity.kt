package org.cardna.presentation.ui.detailcard.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
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
        initData()
        setObserve()
    }

    private fun initData() {
        val id = intent.getIntExtra(BaseViewUtil.CARD_ID, 0)
        detailCardViewModel.getDetailCard(id)
    }

    private fun setObserve() {
        detailCardViewModel.detailCard.observe(this) { detailCard ->
            setSrcWithGlide(detailCard.cardImg!!, binding.ivDetailcardImage)
        }

        detailCardViewModel.type.observe(this) { type ->
            with(binding) {
                when (type) {
                    ME -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_maingreen_stroke_real_black_2dp)
                    }
                    YOU -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_mainpurple_stroke_real_black_2dp)
                        ibtnDetailcardDelete.setBackgroundResource(R.drawable.ic_detail_3dot)
                    }
                    STORAGE -> {
                        tvDetailcardTitle.setBackgroundResource(R.drawable.bg_white_1_5_stroke_real_black_2dp)
                    }
                }
            }
        }
    }

    companion object {
        const val ME = "me"
        const val YOU = "you"
        const val STORAGE = "storage"
    }
}