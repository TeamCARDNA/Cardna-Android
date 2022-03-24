package org.cardna.presentation.ui.mypage.view.setting

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySettingBinding
import org.cardna.presentation.base.BaseViewUtil

class SettingActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setAlarmSwitchListener()
    }

    @SuppressLint("ResourceType")
    private fun setAlarmSwitchListener() {
        with(binding) {
            switchBtnSetting.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    ivSettingAlarm.setBackgroundResource(R.drawable.bg_switch_track_on)
                } else {
                    ivSettingAlarm.setBackgroundResource(R.drawable.bg_switch_track_off)
                }
            }
        }
    }
}