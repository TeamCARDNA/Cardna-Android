package org.cardna.presentation.ui.login.view

// import com.google.android.gms.tasks.OnCompleteListener

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.log.NidLog
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.BuildConfig
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivityLoginBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.ui.setting.view.PrivacyPolicyActivity
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.getErrorLog
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setClickListener()
    }

    private fun setPrivacyPolicyActivity(title: String, text: String): Intent {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        intent.apply {
            putExtra("title", title)
            putExtra("text", text)
        }
        return intent
    }

    private fun setClickListener() {
        with(binding) {
            tvLoginPolicyUseOfTerm.setOnClickListener {
                val intent = setPrivacyPolicyActivity(
                    getString(R.string.policy_text_title),
                    getString(R.string.policy_text)
                )
                startActivity(intent)
            }
            tvLoginPolicyPrivate.setOnClickListener {
                val intent = setPrivacyPolicyActivity(
                    getString(R.string.privacy_text_title),
                    getString(R.string.privacy_text)
                )
                startActivity(intent)
            }
            btnLoginKakao.setOnClickListener {
                setKakaoBtnListener()
            }
            btnLoginNaver.setOnClickListener {
                setNaverLogin()
            }
        }
    }

    private fun setNaverLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDesc = NaverIdLoginSDK.getLastErrorDescription()
                Timber.e("errorCode:$errorCode, errorDesc:$errorDesc")
            }

            override fun onSuccess() {
                val accessToken = NaverIdLoginSDK.getAccessToken() ?: return
                CardNaRepository.naverAccessToken = accessToken
                with(loginViewModel) {
                    getNaverLogin()
                    gotoSetName.observe(this@LoginActivity) { gotoSetName ->
                        if (gotoSetName) startSetNameActivity()
                        else startMainActivity()

                        finish()
                    }
                }
            }
        }
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    //todo 카카오 로그인
    private fun setKakaoBtnListener() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
            } else if (token != null) {
                UserApiClient.instance.me { _, error ->
                    val accessToken = token.accessToken
                    //토큰저장 후 api콜백
                    CardNaRepository.kakaoAccessToken = accessToken
                    Timber.d("login access token : ${accessToken}")
                    with(loginViewModel) {
                        getKakaoLogin()
                        isLogin.observe(this@LoginActivity) { success ->
                            if (success) startMainActivity()
                            else startSetNameActivity()
                            Timber.d("isLogin : ${isLogin.value}")
                            finish()
                        }
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)

        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                this@LoginActivity,
                callback = callback
            )
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startSetNameActivity() {
        val intent = Intent(this, SetNameActivity::class.java)
        startActivity(intent)
    }
}