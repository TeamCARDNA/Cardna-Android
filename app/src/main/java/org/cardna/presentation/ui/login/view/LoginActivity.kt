package org.cardna.presentation.ui.login.view

// import com.google.android.gms.tasks.OnCompleteListener

//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivityLoginBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
import org.cardna.presentation.ui.setting.view.PrivacyPolicyActivity
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

    private fun setPrivacyPolicyActivity(title: String, about: String) {
        Intent(this@LoginActivity, PrivacyPolicyActivity::class.java).apply {
            putExtra("title", title)
            putExtra("about", about)
            startActivity(this)
        }
    }

    private fun setClickListener() {
        with(binding) {
            tvLoginPolicyPrivate.setOnClickListener {
                setPrivacyPolicyActivity(
                    getString(R.string.privacy_text_title),
                    getString(R.string.privacy_text)
                )
            }
            tvLoginPolicyUseOfTerm.setOnClickListener {
                setPrivacyPolicyActivity(
                    getString(R.string.policy_text_title),
                    getString(R.string.policy_text)
                )
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
                        if (gotoSetName) {
                            startSetNameActivity()
                            Timber.e("네이버 로그인 setName으로")
                        }
                        else {
                            startMainActivity()
                            Timber.e("네이버 로그인 Main으로")
                        }

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