package org.cardna.presentation.ui.login.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cardna.R
import org.cardna.databinding.FragmentSecondBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.setSpannableColor

class SecondFragment : BaseViewUtil.BaseFragment<FragmentSecondBinding>(R.layout.fragment_second) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        val inputText = binding.tvSecondAbout.text.toString()
        binding.tvSecondAbout.text =
            requireActivity().setSpannableColor(inputText, R.color.main_purple, 5, 22)
    }
}