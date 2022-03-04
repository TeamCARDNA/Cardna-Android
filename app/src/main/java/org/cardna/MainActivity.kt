package org.cardna

import android.os.Bundle
import com.example.cardna.R
import org.cardna.presentation.base.baseutil.BaseViewUtil
import com.example.cardna.databinding.ActivityMainBinding

class MainActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}