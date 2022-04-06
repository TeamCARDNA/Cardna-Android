package org.cardna.presentation.ui.editcard

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityEditCardBinding
import org.cardna.presentation.base.BaseViewUtil

class EditCardActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityEditCardBinding>(R.layout.activity_edit_card) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}