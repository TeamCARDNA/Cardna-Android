package org.cardna.presentation.ui.mypage

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySettingBinding
import org.cardna.presentation.base.BaseViewUtil

class SettingActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}