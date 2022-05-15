package org.cardna.presentation.ui.setting.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.navercorp.nid.NaverIdLoginSDK
import org.cardna.R
import org.cardna.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.view.LoginActivity
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.shortToast
import org.cardna.presentation.util.showCustomDialog

@AndroidEntryPoint
class SettingActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        setClickListener()
        setAlarmSwitchClickListener()
        setInitAlarmState()
    }

    private fun setInitAlarmState() {
        settingViewModel.pushAlarmOn.observe(this) {
            if (it == false) {
                binding.ivSettingAlarm.setBackgroundResource(R.drawable.bg_switch_track_off)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setAlarmSwitchClickListener() {
        with(binding) {
            switchBtnSetting.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) ivSettingAlarm.setBackgroundResource(R.drawable.bg_switch_track_on)
                else ivSettingAlarm.setBackgroundResource(R.drawable.bg_switch_track_off)
            }
            switchBtnSetting.setOnClickListener {
                settingViewModel?.switchPushAlarm()
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            tvSettingAppInfoVersion.setOnClickListener {
                startActivity(Intent(this@SettingActivity, VersionInfoActivity::class.java))
            }
            tvSettingAppInfoDeveloper.setOnClickListener {
                startActivity(Intent(this@SettingActivity, DeveloperInfoActivity::class.java))
            }
            tvSettingEtcSecession.setOnClickListener {
                startActivity(Intent(this@SettingActivity, SecessionActivity::class.java))
            }
            tvSettingEtcLogout.setOnClickListener {
                showLogOutDialog()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun showLogOutDialog() {
        val dialog = this.showCustomDialog(R.layout.dialog_logout)
        val confirmBtn = dialog.findViewById<Button>(R.id.tv_lougout_dialog_confirm)
        val cancelBtn = dialog.findViewById<Button>(R.id.tv_logout_dialog_cancel)

        confirmBtn.setOnClickListener {
            CardNaRepository.apply {
                if (userSocial == "kakao") {
                    kakaoUserlogOut = true
                    kakaoUserToken = ""
                    kakaoUserRefreshToken = ""
                } else {
//                    NaverIdLoginSDK.logout()
                    naverUserlogOut = true
                    naverUserToken = ""
                    naverUserRefreshToken = ""
                }
            }
            dialog.dismiss()
            shortToast("로그아웃 되었습니다")
            moveToLoginActivity()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun moveToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

