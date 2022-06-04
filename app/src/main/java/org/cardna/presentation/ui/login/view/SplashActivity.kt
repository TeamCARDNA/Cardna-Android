package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.BuildConfig.*
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivitySplashBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.shortToast
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
        with(CardNaRepository) {
//            kakaoUserfirstName = ""
//            kakaoUserToken = ""
//            kakaoUserRefreshToken = ""
//            naverUserfirstName = ""
//            naverUserToken = ""
//            naverUserRefreshToken = ""
//            userToken = ""
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡ맨처음값ㅡㅡㅡㅡㅡㅡㅡㅡㅡ$userToken+$kakaoUserfirstName")
        }
        StatusBarUtil.setStatusBar(this, R.color.black)
        setFullScreen()
        setNextActivity()
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
        // 회원가입
        if (CardNaRepository.kakaoUserfirstName.isEmpty() && CardNaRepository.naverUserfirstName.isEmpty()) {
            Timber.e("ㅡㅡㅡㅡ1.회원가입안함ㅡㅡㅡㅡㅡ${CardNaRepository.kakaoUserfirstName + CardNaRepository.naverUserfirstName}")
            moveOnboarding()
            // 카카오 자동로그인
        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && !CardNaRepository.kakaoUserlogOut) {
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡ2.카카오 회원가입함ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${CardNaRepository.kakaoUserfirstName + !CardNaRepository.kakaoUserlogOut}")
            loginViewModel.getKakaoTokenIssuance()
            Timber.d("KK message : ${loginViewModel.tokenStatusCode.value}")
            loginViewModel.tokenStatusCode.observe(this) {
                when (it) {
                    REFRESH_SUCCESS, ACCESS_NOW -> moveMain()
                    else -> moveOnboarding()
                }
            }
            // 네이버 자동로그인
        } else if (CardNaRepository.naverUserfirstName.isNotEmpty() && !CardNaRepository.naverUserlogOut) {
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡ2.네이버 회원가입함ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ")


            // 토큰재발급 API 호출
            // 여기서 토큰 재발급 API 호출해서 accessToken, refreshToken 유효성 판단
            /*
                    1. naverUserToken 유효
                        => 바로 메인 액티비티로 이동

                    2. naverUserToken 만료, naverUserRefreshToken 유효
                        => 재발급 받은 토큰들 저장
                        => AuthInterpreter 헤더에 accessToken 저장

                    3. 둘다 만료
                        => 네이버 소셜 로그인을 통해 naver accessToken 얻기
                        => 소셜 로그인 API 호출
                        => 발급 받은 naverUserToken, naverUserRefreshToken 저장
             */
            Timber.d("naver 자동로그인: ")

            loginViewModel.getNaverTokenIssuance()

            loginViewModel.tokenStatusCode.observe(this) {
                Timber.d("tokentest 상태 코드 : ${it}")
                if (it == 200) { // 1. 액세스 토큰만 만료 -> 재발급 성공
                    Timber.d("tokentest 재발급 성공")
                    moveMain()
                } else if (it == 400) { // 2. 유효한 토큰
                    Timber.d("tokentest 유효한 토큰입니다.")
                    moveMain()
                } else { // 3. 둘다 만료
                    Timber.d("tokentest 모든 토큰이 만료되었습니다.")
                    moveOnboarding()
                }
            }
            // 카카오나 네이버 로그아웃 했을시
        } else if (CardNaRepository.kakaoUserlogOut || CardNaRepository.naverUserlogOut) {
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
        const val REFRESH_SUCCESS = 200
        const val ACCESS_NOW = 400
        const val NEED_LOGIN = 401
    }
}