package org.cardna.presentation.ui.login.onboardingfragment

import android.os.Bundle
import android.view.View
import com.example.cardna.R
import com.example.cardna.databinding.FragmentSecondBinding
import org.cardna.presentation.base.BaseViewUtil

class SecondFragment : BaseViewUtil.BaseFragment<FragmentSecondBinding>(R.layout.fragment_second) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}