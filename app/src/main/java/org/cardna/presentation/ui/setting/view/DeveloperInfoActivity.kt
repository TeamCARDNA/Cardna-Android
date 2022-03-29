package org.cardna.presentation.ui.setting.view

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityDeveloperInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class DeveloperInfoActivity : BaseViewUtil.BaseAppCompatActivity<ActivityDeveloperInfoBinding>(R.layout.activity_developer_info) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}