package org.cardna.presentation.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentBlankBinding
import org.cardna.databinding.FragmentTestBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.insight.view.InsightFragment
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel

@AndroidEntryPoint
class TestFragment : BaseViewUtil.BaseFragment<FragmentTestBinding>(R.layout.fragment_test) {
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        when (settingViewModel.click.value) {
            1 -> {binding.tv.text="안눌림"}
            2 -> {binding.tv.text="눌림"}
        }
    }

    override fun initView() {
        binding.btn.setOnClickListener {
            if (settingViewModel.click.value == 1)
                settingViewModel.click.value = 2
            if (settingViewModel.click.value == 2)
                settingViewModel.click.value = 1
        }
    }
}