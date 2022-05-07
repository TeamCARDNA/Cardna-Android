package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.navercorp.nid.NaverIdLoginSDK
import org.cardna.R
import org.cardna.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.viewmodel.LoginViewModel
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

    private fun setClickListener() {
        with(binding) {
            tvLoginPolicyUseOfTerm.setOnClickListener {

            }
            tvLoginPolicyPrivate.setOnClickListener {

            }
            btnLoginKakao.setOnClickListener {
                setKakaoLogin()
            }
            btnLoginNaver.setOnClickListener {
                setNaverLogin()
            }
        }
    }

    private fun setNaverLogin() {
        // 1. 네이버 자체 소셜로그인을 통해 naverSocialToken 얻어와서 header token 에 끼우기
        NaverIdLoginSDK.authenticate(this, loginViewModel.oauthLoginCallback)
        CardNaRepository.userToken = loginViewModel.naverSocialUserToken!!

        // 2. 소셜로그인 API 호출
        loginViewModel.getNaverLogin()
        if(loginViewModel.loginType == "signin"){ // 2 - 1. 재로그인 => MainActivity로 이동
            moveOnMain()
        }
        else if(loginViewModel.loginType == "signup"){ // 2 - 2. 회원가입
            moveOnSetName()
        }
    }

    private fun setKakaoLogin() {
        val intent = Intent(this, SetNameActivity::class.java)
        startActivity(intent)
        //ci 되나여?
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
    }

    private fun moveOnMain() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }


    private fun moveOnSetName() {
        Intent(this, SetNameActivity::class.java).apply {
            startActivity(this)
        }
    }

//
//    private fun getDeviceToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                return@OnCompleteListener
//            }
//            // Get new FCM registration token
//            val token = task.result
//            Timber.d("device token $token")
//        })
//    }
}