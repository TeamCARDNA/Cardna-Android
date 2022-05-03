package org.cardna.presentation.ui.login.onboarding

import android.os.Bundle
import android.view.View
import org.cardna.R
import org.cardna.databinding.FragmentFirstBinding
import org.cardna.presentation.base.BaseViewUtil

class FirstFragment : BaseViewUtil.BaseFragment<FragmentFirstBinding>(R.layout.fragment_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}