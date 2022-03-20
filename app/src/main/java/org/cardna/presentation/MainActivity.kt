package org.cardna.presentation

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityMainBinding
import org.cardna.presentation.base.BaseViewUtil

class MainActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}