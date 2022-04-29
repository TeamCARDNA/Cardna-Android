package org.cardna.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.shortToast

@AndroidEntryPoint
class LoginActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
//        setClickListener()
        testKakao()
    }

    private fun setClickListener() {
        with(binding) {
            tvLoginPolicyUseOfTerm.setOnClickListener {

            }
            tvLoginPolicyPrivate.setOnClickListener {

            }
            btnLoginKakao.setOnClickListener {
//                setKakaoLogin()
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
                shortToast("토근 정보 보기 실패")
            } else if (tokenInfo != null) {
                shortToast("토큰 정보 보기 성공")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                getErrorToast(error)
            } else if (token != null) {
                shortToast("login success")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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
            }
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

//        kakaoTalkLogin()
//        startActivity(intent)
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                //Login Fail
//            } else if (token != null) {
//                //Login Success
//                    //앱 키 연결 아직 안함
//                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        UserApiClient.instance.run {
//            if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
//                loginWithKakaoTalk(this@LoginActivity, callback = callback)
//            } else {
//                loginWithKakaoAccount(this@LoginActivity, callback = callback)
//            }
//        }


//    private fun kakaoTalkLogin() {
//        // 카카오톡으로 로그인
//        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//            if (error != null) {
//                Log.e(TAG, "로그인 실패", error)
//            }
//            else if (token != null) {
//                Log.i(TAG, "로그인 성공 ${token.accessToken}")
//            }
//        }
//    }

//    private fun kakaoAccountLogin() {
//        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
//            if (error != null) {
//                Log.e(TAG, "로그인 실패", error)
//            }
//            else if (token != null) {
//                Log.i(TAG, "로그인 성공 ${token.accessToken}")
//            }
//        }
//    }