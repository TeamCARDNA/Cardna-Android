package org.cardna.presentation.ui.setting.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySecessionBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel
import org.cardna.presentation.util.StatusBarUtil

@AndroidEntryPoint
class SecessionActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySecessionBinding>(R.layout.activity_secession) {
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        setObserve()
        setEtcContentListener()
    }

    private fun setObserve() {
        settingViewModel.isSecessionReasonValid.observe(this) { isSecessionReasonValid ->
            with(binding.buttonSecession) {
                if (isSecessionReasonValid) setBackgroundResource(R.drawable.bg_mainpurple_maingreen_gradient_10dp)
                else setBackgroundResource(R.drawable.bg_white_3_10dp)
            }
        }
    }

    private fun setEtcContentListener() {
        binding.etSecessionReason.doAfterTextChanged {
            if (it.isNullOrEmpty()) settingViewModel.setEtcContent("",it.isNullOrEmpty())
            else settingViewModel.setEtcContent(it.toString(),it.isNullOrEmpty())
        }
    }

    companion object {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
    }
}