package org.cardna.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import org.cardna.R
import org.cardna.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class LoginActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityLoginBinding>(R.layout.activity_login) {
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
}