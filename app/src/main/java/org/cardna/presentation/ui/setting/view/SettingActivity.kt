package org.cardna.presentation.ui.setting.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.amplitude.api.Amplitude
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import org.cardna.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.view.LoginActivity
import org.cardna.presentation.ui.login.view.OnBoardingActivity
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel
import org.cardna.presentation.util.shortToast
import org.cardna.presentation.util.showCustomDialog

@AndroidEntryPoint
class SettingActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by viewModels()
    private val cardnaMail = "https://docs.google.com/forms/d/e/1FAIpQLSd9KrWFdzWDEvYfq2ein6reL4ZMjqTq_JQFhODSwEBGwkv7kg/viewform"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {
        setUserAccount()
        setClickListener()
        setAlarmSwitchClickListener()
        setInitAlarmState()
    }

    private fun setUserAccount() {
        with(binding) {
            if (CardNaRepository.userSocial == "kakao") {
                ivAccountInfoSocialLogin.setImageResource(R.drawable.ic_logo_kako)
                tvAccountInfoSocialLoginUser.text = "카카오"
            } else {
                ivAccountInfoSocialLogin.setImageResource(R.drawable.ic_logo_naver)
                tvAccountInfoSocialLoginUser.text = "네이버"
            }
        }
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
            tvSettingQuestionByEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cardnaMail))
                startActivity(intent)
            }

            tvSettingAppInfoVersion.setOnClickListener {
                startActivity(Intent(this@SettingActivity, VersionInfoActivity::class.java))
            }
            tvSettingAppInfoDeveloper.setOnClickListener {
                startActivity(Intent(this@SettingActivity, DeveloperInfoActivity::class.java))
            }
            tvSettingAppInfoPrivacyPolicy.setOnClickListener {
                startActivity(Intent(this@SettingActivity, PrivacyPolicyActivity::class.java))
            }
            tvSettingAppInfoServiceOperationPolicy.setOnClickListener {
                startActivity(Intent(this@SettingActivity, ServiceOperationActivity::class.java))
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
                    naverUserlogOut = true
                    naverUserToken = ""
                    naverUserRefreshToken = ""
                    NidOAuthLogin().logout()
                }
            }
            Amplitude.getInstance().logEvent("My_Logout ")
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


