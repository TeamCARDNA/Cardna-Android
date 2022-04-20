package org.cardna.presentation.ui.login

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySplashBinding
import org.cardna.presentation.base.BaseViewUtil

class SplashActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}