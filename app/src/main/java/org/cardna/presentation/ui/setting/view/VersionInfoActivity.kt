package org.cardna.presentation.ui.setting.view

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityVersionInfoBinding
import org.cardna.presentation.base.BaseViewUtil

class VersionInfoActivity : BaseViewUtil.BaseAppCompatActivity<ActivityVersionInfoBinding>(R.layout.activity_version_info) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}