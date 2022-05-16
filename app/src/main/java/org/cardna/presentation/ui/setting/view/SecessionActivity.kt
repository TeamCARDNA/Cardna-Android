package org.cardna.presentation.ui.setting.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository.naverUserRefreshToken
import org.cardna.data.local.singleton.CardNaRepository.naverUserToken
import org.cardna.data.local.singleton.CardNaRepository.naverUserfirstName
import org.cardna.data.local.singleton.CardNaRepository.userSocial
import org.cardna.data.local.singleton.CardNaRepository.userUuid
import org.cardna.databinding.ActivitySecessionBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.view.LoginActivity
import org.cardna.presentation.ui.login.view.OnBoardingActivity
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel
import org.cardna.presentation.util.KeyboardVisibilityUtils
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.shortToast

@AndroidEntryPoint
class SecessionActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySecessionBinding>(R.layout.activity_secession) {
    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
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
        settingViewModel.isDeleteUserSuccess.observe(this) {
            if (it) {
                shortToast("탈퇴가 완료되었습니다")

                /*       // 네이버계정에서 탈퇴 시
                       NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
                           override fun onSuccess() {
                               //서버에서 토큰 삭제에 성공한 상태입니다.
                           }
                           override fun onFailure(httpStatus: Int, message: String) {
                               // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                               // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                               Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                               Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                           }
                           override fun onError(errorCode: Int, message: String) {
                               // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                               // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                               onFailure(errorCode, message)
                           }
                       })*/
                moveOnBoardingActivity()
            }
        }
    }

    private fun moveOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java).apply {
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