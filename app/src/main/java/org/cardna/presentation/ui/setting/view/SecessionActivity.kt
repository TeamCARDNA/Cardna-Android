package org.cardna.presentation.ui.setting.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.navercorp.nid.oauth.NidOAuthLogin
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.data.local.singleton.CardNaRepository.userSocial
import org.cardna.databinding.ActivitySecessionBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.view.LoginActivity
import org.cardna.presentation.ui.login.view.OnBoardingActivity
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel
import org.cardna.presentation.util.KeyboardVisibilityUtils
import org.cardna.presentation.util.shortToast
import timber.log.Timber

@AndroidEntryPoint
class SecessionActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySecessionBinding>(R.layout.activity_secession) {
    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {
        setEtcContentListener()
        setDeleteUserObserve()
        setHideKeyboard()
    }


    private fun setEtcContentListener() {
        with(binding.etSecessionReason) {
            doAfterTextChanged {
                if (it.isNullOrEmpty()) settingViewModel.setEtcContent("", it.isNullOrEmpty())
                else settingViewModel.setEtcContent(it.toString(), it.isNullOrEmpty())
            }
            setOnClickListener {
                setHideKeyboard()
                setEditTextWhenOpenKeyboard()
            }
        }
    }

    private fun setDeleteUserObserve() {
        binding.btnSession.setOnClickListener {
            if (userSocial == "kakao") {
                settingViewModel.deleteUser()
            } else {  // naver 일 시, 회원탈퇴 버튼 누르면, 네이버 자체 회원탈퇴 실행
                settingViewModel.deleteNaverUser(this)
            }
        }

        // 네이버 자체 회원탈퇴
        settingViewModel.isDeleteNaverServerUserSuccess.observe(this) {
            if (it) { // 성공 시, 자체 서버 회원탈퇴 실행
                settingViewModel.deleteUser()
            } else { // 실패 시,
                // 로그아웃 시 진행되는 작업들을 해주고, loginActivity 로 가서 재로그인 할 수 있도록
                shortToast("네트워크 신호가 약해 회원탈퇴에 실패했습니다. 재로그인이 필요합니다 :(")
                CardNaRepository.naverUserlogOut = true
                CardNaRepository.naverUserToken = ""
                CardNaRepository.naverUserRefreshToken = ""
//                NidOAuthLogin().logout()
                moveOnLoginActivity()
            }
        }

        // 자체 서버 회원탈퇴 성공 시, 토스트 메시지 출력 후 온보딩으로 이동
        settingViewModel.isDeleteUserSuccess.observe(this) {
            if (it) {
                shortToast("탈퇴가 완료되었습니다 :)")
                Timber.e("최종 회원탈퇴 성공")
                moveOnBoardingActivity()
            } else {  // 이 경우에는 어떤 처리를 해줘야 할까 ?
                shortToast("탈퇴에 실패하였습니다. :(")
                Timber.e("최종 회원탈퇴 실패")
            }
        }
    }

    private fun moveOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun moveOnLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun setHideKeyboard() {
        binding.ctlSeccionContaier.setOnClickListener {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.etSecessionReason.windowToken, 0)
        }
    }

    private fun setEditTextWhenOpenKeyboard() {
        val param = binding.etSecessionReason.layoutParams as ViewGroup.MarginLayoutParams
        keyboardVisibilityUtils = KeyboardVisibilityUtils(
            window,
            onShowKeyboard = { keyboardHeight, _ ->
                param.setMargins(
                    param.leftMargin,
                    param.topMargin,
                    param.rightMargin,
                    keyboardHeight - 300
                )
                binding.etSecessionReason.layoutParams = param
            },
            onHideKeyboard = {
                param.setMargins(param.leftMargin, param.topMargin, param.rightMargin, 30)
                binding.etSecessionReason.layoutParams = param
            }
        )
    }

    companion object {
        const val SECESSION_REASON_ONE = 1
        const val SECESSION_REASON_TWO = 2
        const val SECESSION_REASON_THREE = 3
        const val SECESSION_REASON_FOUR = 4
        const val SECESSION_REASON_FIVE = 5
        const val SECESSION_REASON_SIX = 6
    }
}