package org.cardna.presentation.ui.setting.view

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivityPrivacyPolicyBinding
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class PrivacyPolicyActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityPrivacyPolicyBinding>(R.layout.activity_privacy_policy) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        val title = intent.getStringExtra("title")
        val about = intent.getStringExtra("about")
        with(binding) {
            tvPrivacyPolicyTitle.text = title
            tvPrivacyPolicyAbout.text = about
        }
    }
}