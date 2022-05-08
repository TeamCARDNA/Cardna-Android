package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import com.navercorp.nid.NaverIdLoginSDK
import org.cardna.BuildConfig.*
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
        initNaverLogin()
        setFullScreen()
        setNextActivity()
    }

    // naverLogin
    private fun initNaverLogin(){
        Timber.d("initNaverLogin")
//        NaverIdLoginSDK.initialize(this, NAVER_API_CLIENT_ID, NAVER_API_CLIENT_SECRET, NAVER_API_APP_NAME)
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

    private fun autoKakaoLoginCheck() {
        loginViewModel.message.observe(this) { message ->
            Timber.d("message : $message")
            when (message) {
                ACCESS_NOW, REFRESH_SUCCESS -> {
                    moveMain()
                }
                else -> {
                    moveOnboarding()
                }
            }
        }
    }

    private fun setNextActivity() {
        if (CardNaRepository.kakaoUserfirstName.isEmpty() && CardNaRepository.naverUserfirstName.isEmpty()) {
            //모든 소셜에서 이름이 없으면 -> 회원가입 안함
            moveOnboarding()
            Timber.d("kakaoUserFirstName : ${CardNaRepository.kakaoUserfirstName}")
        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && !CardNaRepository.kakaoUserlogOut) {
            //카카오로 자동로그인
            //1. 카카오에 이름잇음+카카오에서 로그아웃 안함 -> 이미 완성된거
            //firebaseToken, fcmToken 은 회원가입때 이미 넣어줬겠지?

//            loginViewModel.getKakaoLogin()
//            loginViewModel.getTokenIssuance()
//            CardNaRepository.userToken = CardNaRepository.kakaoUserToken
            autoKakaoLoginCheck()

            //네이버로 자동로그인
            //2.네이버에 이름잇음+네이버에서 로그아웃 안함
        } else if (CardNaRepository.naverUserfirstName.isNotEmpty() && !CardNaRepository.naverUserlogOut) {
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
            loginViewModel.getNaverTokenIssuance()

            if(loginViewModel.issuanceMessage == ""){ // 2. accessToken 만료, refresh 토큰 유효할 때 갱신 성공했을 것
                moveMain()
            }
            else if(loginViewModel.issuanceMessage == "유효한 토큰입니다."){ // 1. accessToken 유효
                moveMain()
            }
            else if(loginViewModel.issuanceMessage == "모든 토큰이 만료되었습니다.") { // 3. 둘다 만료

                // 네이버 소셜 로그인을 통해 naver accessToken 얻기
//                NaverIdLoginSDK.authenticate(this, loginViewModel.oauthLoginCallback)

                // 소셜로그인 API를 호출하기 위해 헤더의 토큰을 naver 소셜 토큰으로 갈아끼움
                CardNaRepository.userToken = loginViewModel.naverSocialUserToken!!

                // 소셜 로그인 API 호출 => 발급 받은 naverUserToken, naverUserRefreshToken 저장, 헤더 갈아끼우기
                loginViewModel.getNaverLogin()

                // Main으로 이동
                moveMain()
            }
            else{

            }
            //로그아웃
        } else if (CardNaRepository.kakaoUserlogOut || CardNaRepository.naverUserlogOut) {

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
    }
}