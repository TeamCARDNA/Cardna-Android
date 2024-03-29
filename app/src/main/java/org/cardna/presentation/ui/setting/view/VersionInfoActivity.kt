package org.cardna.presentation.ui.setting.view

import android.graphics.Color
import android.os.Bundle
import org.cardna.R
import org.cardna.databinding.ActivityVersionInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.BuildConfig
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.StatusBarUtil

@AndroidEntryPoint
class VersionInfoActivity : BaseViewUtil.BaseAppCompatActivity<ActivityVersionInfoBinding>(R.layout.activity_version_info) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        binding.tvVersionInfo.text =  BuildConfig.VERSION_NAME
    }
}