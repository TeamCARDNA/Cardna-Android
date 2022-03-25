package org.cardna.presentation.ui.insight.view

import android.os.Bundle
import android.view.View
import com.example.cardna.R
import com.example.cardna.databinding.FragmentInsightBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.insight.adapter.InsightAdapter

@AndroidEntryPoint
class InsightFragment : BaseViewUtil.BaseFragment<FragmentInsightBinding>(R.layout.fragment_insight) {
    private lateinit var insightAdapter: InsightAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        setInsightAdapter()
    }

    private fun setInsightAdapter() {
        val fragmentList = listOf(OpenAreaFragment(), BlindAreaFragment())
        insightAdapter = InsightAdapter(requireActivity())
        insightAdapter.fragments.addAll(fragmentList)
        binding.vpInsight.adapter = insightAdapter
    }
}
