package org.cardna.presentation.ui.detailcard.view

import android.os.Bundle
import com.example.cardna.databinding.ActivityCardShareBinding
import com.example.cardna.databinding.ActivityDetailCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import com.example.cardna.R

@AndroidEntryPoint
class CardShareActivity : BaseViewUtil.BaseAppCompatActivity<ActivityCardShareBinding>(R.layout.activity_card_share) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}