package org.cardna.presentation.ui.login.onboarding

import android.os.Bundle
import android.view.View
import org.cardna.R
import org.cardna.databinding.FragmentThirdBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.setSpannableColor

class ThirdFragment : BaseViewUtil.BaseFragment<FragmentThirdBinding>(R.layout.fragment_third) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        val inputText = binding.tvThirdAbout.text.toString()
        binding.tvThirdAbout.text =
            requireActivity().setSpannableColor(inputText, R.color.main_green, 9, 20)
    }
}