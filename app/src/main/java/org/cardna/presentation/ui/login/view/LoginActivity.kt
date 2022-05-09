package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
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
        StatusBarUtil.setStatusBar(this, R.color.black)
        setClickListener()
        getDeviceToken()
//        testKakao()
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            CardNaRepository.fireBaseToken = token.toString()
            Timber.d("fcm token ${CardNaRepository.fireBaseToken}")
        })
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
//                testKakao()
            }
            btnLoginNaver.setOnClickListener {
//                setNaverLogin()
            }

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

    private fun setKakaoBtnListener() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                getErrorLog(error)
            } else if (token != null) {
                //카카오 로그인 콜백
                with(CardNaRepository) {
                    kakaoAccessToken = token.accessToken
                    Timber.d("kakaoAccessToken : $kakaoAccessToken")
                }
                with(loginViewModel) {
                    getKakaoLogin()
                    isLogin.observe(this@LoginActivity) { success ->
                        if (success) {
                            startMainActivity()
                        } else {
                            startSetNameActivity()
                        }
                    }
                }
                finish()
            }
        }
        binding.btnLoginKakao.setOnClickListener {
            val context = this
            with(UserApiClient.instance) {
                if (isKakaoTalkLoginAvailable(context)) {
                    //kakaoTalk (o) ->  kakaoTalk login
                    loginWithKakaoTalk(context, callback = callback)
                } else {
                    //kakaoTalk (x) kakao account login
                    loginWithKakaoAccount(context, callback = callback)
                }
                accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        getErrorLog(error)
                    } else if (tokenInfo != null) {
                        CardNaRepository.userUuid = tokenInfo.id.toString()
                        Timber.d("userUuid : ${tokenInfo.id}")
                        Timber.d("userUuid : ${CardNaRepository.userUuid}")
                    }
                }
            }
        }
    }

    private fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Timber.d("logout success")
            } else {
                Timber.d("logout fail")
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }
}