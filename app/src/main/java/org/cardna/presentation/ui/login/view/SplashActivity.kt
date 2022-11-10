package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.BuildConfig
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivitySplashBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.util.StatusBarUtil


@AndroidEntryPoint
class SplashActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        checkAppUpdate()
    }

    override fun initView() {
        loginViewModel.setTotalCardCnt()
        StatusBarUtil.setStatusBar(this, R.color.black)
        setFullScreen()
    }

    private fun checkAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                val nowVersionCode = BuildConfig.VERSION_CODE
                val newVersionCode = appUpdateInfo.availableVersionCode()

                if ((newVersionCode - nowVersionCode) >= 5) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                } else {
                    setNextActivity()
                }
            } else {
                setNextActivity()
            }
        }

        appUpdateInfoTask.addOnFailureListener {
            setNextActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                checkAppUpdate()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
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
        if (CardNaRepository.kakaoUserfirstName.isEmpty() && CardNaRepository.naverUserfirstName.isEmpty()) {
            moveOnboarding()
        } else if (CardNaRepository.kakaoUserfirstName.isNotEmpty() && !CardNaRepository.kakaoUserlogOut) {
            loginViewModel.getKakaoTokenIssuance()
            loginViewModel.tokenStatusCode.observe(this) {
                when (it) {
                    REFRESH_SUCCESS, ACCESS_NOW -> moveMain()
                    else -> {
                        moveOnboarding()
                    }
                }
            }
        } else if (CardNaRepository.naverUserfirstName.isNotEmpty() && !CardNaRepository.naverUserlogOut) {
            loginViewModel.getNaverTokenIssuance()

            loginViewModel.tokenStatusCode.observe(this) {
                if (it == 200) { // 1. 액세스 토큰만 만료 -> 재발급 성공
                    moveMain()
                } else if (it == 400) { // 2. 유효한 토큰
                    moveMain()
                } else { // 3. 둘다 만료
                    moveOnboarding()
                }
            }
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
        const val MY_REQUEST_CODE = 1229
    }
}