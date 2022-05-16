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
         //   kakaoUserfirstName=""
         //   kakaoUserToken=""
         //   kakaoUserRefreshToken=""
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
        //todo  회원가입
        if (CardNaRepository.kakaoUserfirstName.isEmpty() && CardNaRepository.naverUserfirstName.isEmpty()) {
            Timber.e("ㅡㅡㅡㅡ1.회원가입안함ㅡㅡㅡㅡㅡ${CardNaRepository.kakaoUserfirstName + CardNaRepository.naverUserfirstName}")
            moveOnboarding()

            //todo 카카오 자동로그인
        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && !CardNaRepository.kakaoUserlogOut) {
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡ2.카카오 회원가입함ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${CardNaRepository.kakaoUserfirstName + !CardNaRepository.kakaoUserlogOut}")
            CardNaRepository.userToken = CardNaRepository.kakaoUserToken
            moveMain()

/*            firebaseToken, fcmToken 은 회원가입때 이미 넣어줬겠지?
            loginViewModel.getKakaoLogin()
            loginViewModel.getTokenIssuance()
            CardNaRepository.userToken = CardNaRepository.kakaoUserToken
            autoKakaoLoginCheck()*/

            //todo 카카오 로그아웃했을 시
        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && CardNaRepository.kakaoUserlogOut) {
            moveOnboarding()

            //TODO 네이버 자동로그인
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

            if (loginViewModel.issuanceMessage == "") { // 2. accessToken 만료, refresh 토큰 유효할 때 갱신 성공했을 것
                moveMain()
            } else if (loginViewModel.issuanceMessage == "유효한 토큰입니다.") { // 1. accessToken 유효
                moveMain()
            } else if (loginViewModel.issuanceMessage == "모든 토큰이 만료되었습니다.") { // 3. 둘다 만료
                val oauthLoginCallback = object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                        Timber.d("naver onSuccess: ")
                        loginViewModel.setNaverSocialUserToken(NaverIdLoginSDK.getAccessToken()!!)
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Timber.d("naver ErrorCode : ${errorCode}")
                        Timber.d("naver ErrorDescription : ${errorDescription}")
                    }

                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }
                }

                // 네이버 소셜 로그인을 통해 naver accessToken 얻기
                NaverIdLoginSDK.authenticate(this, oauthLoginCallback)

                // 소셜로그인 API를 호출하기 위해 헤더의 토큰을 naver 소셜 토큰으로 갈아끼움
                CardNaRepository.userToken = loginViewModel.naverSocialUserToken!!

                // 소셜 로그인 API 호출 => 발급 받은 naverUserToken, naverUserRefreshToken 저장, 헤더 갈아끼우기
                loginViewModel.getNaverLogin()

                // Main으로 이동
                moveMain()
            } else {

            }
            //로그아웃

        } else if (CardNaRepository.kakaoUserlogOut || CardNaRepository.naverUserlogOut) {
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