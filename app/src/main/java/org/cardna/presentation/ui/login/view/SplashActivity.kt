package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivitySplashBinding
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.util.StatusBarUtil
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, R.color.black)
        initData()
        setFullScreen()
        setNextActivity()
    }

    private fun initData() {
        loginViewModel.getTokenIssuance()
//        loginViewModel.getKakaoLogin()
    }

    private fun setFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
        }
    }

    private fun setNextActivity() {
        if (CardNaRepository.kakaoUserfirstName.isEmpty() && CardNaRepository.naverUserfirstName.isEmpty()) {
            //모든 소셜에서 이름이 없으면->회원가입 안함
            moveOnboarding()
            with(CardNaRepository) {
                Timber.d("if kakao : ${kakaoUserfirstName.isEmpty()}")
                Timber.d("if naver : ${naverUserfirstName.isEmpty()}")
                Timber.d("if : ${kakaoUserfirstName.isEmpty() || naverUserfirstName.isEmpty()}")
            }
            Timber.d("if kakaoUserFirstName : ${CardNaRepository.kakaoUserfirstName}")

        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && !CardNaRepository.kakaoUserlogOut) {
            //카카오로 자동로그인
            //1. 카카오에 이름잇음+카카오에서 로그아웃 안함 -> 이미 완성된거
            //firebaseToken, fcmToken 은 회원가입때 이미 넣어줬겠지?
//            CardNaRepository.userToken = CardNaRepository.kakaoUserToken
            CardNaRepository.userToken = CardNaRepository.kakaoUserToken
            moveMain()
//            autoKakaoLoginCheck()
        } else if (CardNaRepository.naverUserfirstName.isNotEmpty() && !CardNaRepository.naverUserlogOut) {
            //네이버로 자동로그인
            //2.네이버에 이름잇음+네이버에서 로그아웃 안함
            Timber.d("kakaoUserFirstName : ${CardNaRepository.naverUserfirstName}")
            CardNaRepository.userToken = CardNaRepository.naverUserToken
            moveMain()
        } else if (CardNaRepository.kakaoUserlogOut || CardNaRepository.naverUserlogOut) {
            //로그아웃
            Timber.d("else if logout kakaoUserFirstName : ${CardNaRepository.naverUserfirstName}")
            moveOnboarding()
        } else {
            moveOnboarding()
        }
    }

    private fun moveOnboarding() {
        val intent = Intent(baseContext, OnBoardingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startNextActivityWithHandling(intent)
    }

    private fun moveMain() {
        val intent = Intent(baseContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startNextActivityWithHandling(intent)
    }

    private fun startNextActivityWithHandling(intent: Intent) {
        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity(intent)
                finish()
            }, 2000)
    }

    companion object {
        const val REFRESH_SUCCESS = "토큰 재발급 성공"
        const val ACCESS_NOW = "유효한 토큰입니다."
        const val LOGIN_SUCCESS = "로그인 성공"
    }
}