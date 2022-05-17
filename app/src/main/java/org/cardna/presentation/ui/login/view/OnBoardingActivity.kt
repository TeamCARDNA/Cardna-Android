package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.R
import org.cardna.databinding.ActivityOnBoardingBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.adapter.OnBoardingAdapter
import org.cardna.presentation.ui.login.onboarding.FirstFragment
import org.cardna.presentation.ui.login.onboarding.FourthFragment
import org.cardna.presentation.ui.login.onboarding.SecondFragment
import org.cardna.presentation.ui.login.onboarding.ThirdFragment
import org.cardna.presentation.util.StatusBarUtil


@AndroidEntryPoint
class OnBoardingActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    val fragmentList =
        listOf(FirstFragment(), SecondFragment(), ThirdFragment(), FourthFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initAdapter()
        setClickListener()
    }

    private fun initAdapter() {
        onBoardingAdapter = OnBoardingAdapter(this)
        onBoardingAdapter.fragmentList.addAll(fragmentList)
        with(binding.vpOnboardingContainer) {
            adapter = onBoardingAdapter
            binding.sdiOnboardingIndicator.setViewPager2(this)
        }
    }

    private fun setLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setClickListener() {
        with(binding) {
            tvFirstSkip.setOnClickListener {
                setLoginActivity()
            }
            tvFirstNext.setOnClickListener {
                val position = vpOnboardingContainer.currentItem
                if (position == fragmentList.size - 1) {
                    setLoginActivity()
                } else {
                    vpOnboardingContainer.setCurrentItem(position + 1, true)
                }
            }
        }
    }
}