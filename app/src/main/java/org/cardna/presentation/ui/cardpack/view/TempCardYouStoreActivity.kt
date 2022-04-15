package org.cardna.presentation.ui.cardpack.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityTempCardYouStoreBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class TempCardYouStoreActivity : BaseViewUtil.BaseAppCompatActivity<ActivityTempCardYouStoreBinding>(R.layout.activity_temp_card_you_store) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}