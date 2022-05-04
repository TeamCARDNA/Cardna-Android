package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
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
import org.cardna.presentation.util.shortToast
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
        testKakao()
    }

    private fun getDeviceToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Timber.d("device token $token")
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
//                setKakaoLogin()
//                testKakao()
            }
            btnLoginNaver.setOnClickListener {
                setNaverLogin()
            }

        }
    }

    private fun setNaverLogin() {

    }

    private fun setKakaoLogin() {
        val intent = Intent(this, SetNameActivity::class.java)
    }

    private fun testKakao() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                //자동로그인 안됨 -> 로그아웃 or 만료  , 비회원
                shortToast("토근 정보 보기 실패")
            } else if (tokenInfo != null) {
                //이미 로그인 되어있는 상태 -> 자동 로그인
                Timber.d("tokenInfo : $tokenInfo")
                shortToast("토큰 정보 보기 성공")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                startActivity(intent)
//                logout()
                finish()
            }
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                getErrorToast(error)
            } else if (token != null) {
                Timber.d("token : ${token.accessToken}")
                with(CardNaRepository) {
                    //유저 토큰은 post로 받아와야함
//                    userToken = loginViewModel.postSignUp(RequestSignUpData(
//                        social = "kakao",
//                        uuid = kakaoUserToken,
//                        lastName =
//
//                        )).toString()
                    kakaoUserToken = token.accessToken
                    kakaoUserRefreshToken = token.refreshToken
                }
                //회원 가입뷰에서 넘어감
                shortToast("login success")
                val intent = Intent(this, SetNameActivity::class.java)
                intent.putExtra("social", "kakao")
                loginViewModel.setToken(token)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            } else {
                shortToast("token lost")
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
            }
        }
    }

    private fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                shortToast("logout success")
            } else {
                shortToast("logout fail")
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
//            finish()
        }
    }

    private fun getErrorToast(error: Throwable) {
        when {
            error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                shortToast("접근이 거부 됨(동의 취소)")
            }
            error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                shortToast("유효하지 않은 앱")
            }
            error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                shortToast("인증 수단이 유효하지 않아 인증할 수 없는 상태")
            }
            error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                shortToast("요청 파라미터 오류")
            }
            error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                shortToast("유효하지 않은 scope ID")
            }
            error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                shortToast("설정이 올바르지 않음(android key hash)")
            }
            error.toString() == AuthErrorCause.ServerError.toString() -> {
                shortToast("서버 내부 에러")
            }
            error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                shortToast("앱이 요청 권한이 없음")
            }
            else -> { // Unknown
                shortToast("기타 에러")
            }
        }
    }
}