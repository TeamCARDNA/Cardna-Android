package org.cardna.presentation.ui.insight.view

import android.os.Bundle
import android.view.View
import com.example.cardna.R
import com.example.cardna.databinding.FragmentInsightBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class InsightFragment : BaseViewUtil.BaseFragment<FragmentInsightBinding>(R.layout.fragment_insight) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}
