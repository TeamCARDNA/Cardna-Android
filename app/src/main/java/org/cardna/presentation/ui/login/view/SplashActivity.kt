package org.cardna.presentation.ui.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.BuildConfig
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivitySplashBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import kotlin.math.absoluteValue


@AndroidEntryPoint
class SplashActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        // 강제 업데이트 로직을 여기다 둬야 할 듯.
        // initView 호출 후 로티가 띄워지고, 다음 플로우로 이동하기 전, 업데이트 있는지 판단
        checkAppUpdate()

//        setNextActivity()
    }

    override fun initView() {
        loginViewModel.setTotalCardCnt()


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
    }

    private fun checkAppUpdate() {
        Timber.e("인앱업데이트 1")
        appUpdateManager = AppUpdateManagerFactory.create(this)

        Timber.e("인앱업데이트 2")
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        Timber.e("인앱업데이트 3")
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            Timber.e("인앱업데이트 4")

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) { // 업데이트 가 있는 경우
                Timber.e("인앱업데이트 있음")

                val nowVersionCode = BuildConfig.VERSION_CODE // 현재 versionCode
                val newVersionCode = appUpdateInfo.availableVersionCode() // 업데이트 버전의 versionCode

                if ((newVersionCode - nowVersionCode) >= 5) { // 강제 업데이트 할 정도로 큰 업데이트라면
                    Timber.e("인앱업데이트 있는데 강제 업데이트")
                    appUpdateManager.startUpdateFlowForResult( // 강제 업데이트 실행
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                } else { //그게 아니라면
                    // 선택적 업데이트이므로 아무 실행도 하지 않고, 다음 플로우로 앱 진행
                    Timber.e("인앱업데이트 있는데 강제 업데이트 아님")
                    setNextActivity()
                }
            } else { // 업데이트가 없는 경우
                Timber.e("인앱업데이트 없음")
                // 아무 실행도 하지 않고, 다음 플로우로 앱 진행
                setNextActivity()
            }
        }

        appUpdateInfoTask.addOnFailureListener {
            Timber.e("인앱업데이트 Fail")
            setNextActivity()
        }
    }


    // 인앱 immediate update 팝업으로 인텐트 이동 했다가 백버튼이나 x버튼 눌러서 다시 스플래쉬로 돌아왔을 때
    // 업데이트가 안되어있다면, 다시 업데이트 실행

    // 업데이트 시작 전, 취소 ?
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.e("인앱업데이트 onActivityResult")
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) { // 업데이트 실패 또는 취소 ?
                // 다시 인앱 즉시 업데이트 팝업창 실행
                checkAppUpdate()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    // 업데이트가 중단 된 경우, 앱이 다시 foreground로 돌아왔을 때, 업데이트가 진행중이면 다시 업데이트 재개

    // 업데이트 중간에 뒤로가거나 백그라운드로 가거나 하면 다시 업데이트 화면으로 강제로 이동하도록?
    override fun onResume() {
        Timber.e("인앱업데이트 onResume")
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                }
            }
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
                    else -> {
                        Timber.d("KKK $it")
                        moveOnboarding()
                    }
                }
            }
            // 네이버 자동로그인
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

        const val MY_REQUEST_CODE = 1229
    }
}