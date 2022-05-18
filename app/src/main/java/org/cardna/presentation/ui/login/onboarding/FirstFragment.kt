package org.cardna.presentation.ui.login.onboarding

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import com.google.android.material.color.MaterialColors.getColor
import org.cardna.R
import org.cardna.databinding.FragmentFirstBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.setSpannableColor
import timber.log.Timber


class FirstFragment : BaseViewUtil.BaseFragment<FragmentFirstBinding>(R.layout.fragment_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        val inputText = binding.tvFirstAbout.text.toString()
        binding.tvFirstAbout.text =
            requireActivity().setSpannableColor(inputText, R.color.main_green, 5, 23)
    }
}