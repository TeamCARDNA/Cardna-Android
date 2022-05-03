package org.cardna.presentation.ui.setting.view

import android.os.Bundle
import org.cardna.R
import org.cardna.databinding.ActivityServiceOperationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class ServiceOperationActivity : BaseViewUtil.BaseAppCompatActivity<ActivityServiceOperationBinding>(R.layout.activity_service_operation) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}