package org.cardna.presentation.ui.insight.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentInsightBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.insight.adapter.InsightAdapter
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel

@AndroidEntryPoint
class InsightFragment : BaseViewUtil.BaseFragment<FragmentInsightBinding>(R.layout.fragment_insight) {
    private lateinit var insightAdapter: InsightAdapter
    private val insightViewModel: InsightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        initData()
        setInsightAdapter()
        setCurrentPositionObserve()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    fun initData() {
        insightViewModel.getInsight()
    }

    private fun setInsightAdapter() {
        val fragmentList = listOf(OpenAreaFragment(), BlindAreaFragment())
        insightAdapter = InsightAdapter(requireActivity())
        insightAdapter.fragments.addAll(fragmentList)
        binding.vpInsight.adapter = insightAdapter
    }

    private fun setCurrentPositionObserve() {
        insightViewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition ->
            with(binding.vpInsight) {
                if (currentPosition == OPEN_AREA) setCurrentItem(2, true)
                else setCurrentItem(currentItem - 1, true)
            }
        }
    }

    companion object {
        const val OPEN_AREA = "OPEN_AREA"
        const val BLIND_AREA = "BLIND_AREA"
    }
}
