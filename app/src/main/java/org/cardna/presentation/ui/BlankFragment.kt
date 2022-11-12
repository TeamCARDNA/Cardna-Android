package org.cardna.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentBlankBinding
import org.cardna.databinding.FragmentBlindAreaBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.insight.view.InsightFragment
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel
import org.cardna.presentation.ui.setting.TestFragment

@AndroidEntryPoint
class BlankFragment : BaseViewUtil.BaseFragment<FragmentBlankBinding>(R.layout.fragment_blank) {
    private val insightViewModel: InsightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        childFragmentManager.beginTransaction().add(R.id.fcv,InsightFragment())
            .commit()

        childFragmentManager.beginTransaction().replace(R.id.fcv,TestFragment())
            .addToBackStack(null)
            .commit()
    }
}