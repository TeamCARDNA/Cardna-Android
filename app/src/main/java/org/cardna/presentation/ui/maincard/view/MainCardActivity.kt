package org.cardna.presentation.ui.maincard.view

import android.os.Bundle
import androidx.activity.viewModels
import com.example.cardna.R
import com.example.cardna.databinding.ActivityMainCardBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel

class MainCardActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainCardBinding>(R.layout.activity_main_card) {
    private val mainCardViewModel: MainCardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initAdapter()
        initData()
    }

    private fun initData() {

    }

    private fun initAdapter() {

    }
}