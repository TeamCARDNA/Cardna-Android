package org.cardna.presentation.ui.login.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cardna.R
import org.cardna.databinding.FragmentFourthBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.setGradientText

class FourthFragment : BaseViewUtil.BaseFragment<FragmentFourthBinding>(R.layout.fragment_fourth) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        setTextGradient()
    }

    private fun setTextGradient() {
        with(binding.tvFourthTitle) {
            val gradientText = requireActivity().setGradientText(this.text.toString())
            this.text = gradientText
        }
    }
}