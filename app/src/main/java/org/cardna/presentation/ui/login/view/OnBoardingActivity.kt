package org.cardna.presentation.ui.login.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import org.cardna.R
import org.cardna.databinding.ActivityOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.login.adapter.OnBoardingAdapter
import org.cardna.presentation.ui.login.onboardingfragment.FirstFragment
import org.cardna.presentation.ui.login.onboardingfragment.FourthFragment
import org.cardna.presentation.ui.login.onboardingfragment.SecondFragment
import org.cardna.presentation.ui.login.onboardingfragment.ThirdFragment
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
        StatusBarUtil.setStatusBar(this, Color.BLACK)
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

    private fun setMainActivity() {
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setClickListener() {
        with(binding) {
            tvFirstSkip.setOnClickListener {
                setMainActivity()
            }
            tvFirstNext.setOnClickListener {
                val position = vpOnboardingContainer.currentItem
                if (position == fragmentList.size - 1) {
                    setMainActivity()
                } else {
                    vpOnboardingContainer.setCurrentItem(position + 1, true)
                }
            }
        }
    }
}