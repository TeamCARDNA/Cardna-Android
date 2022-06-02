package org.cardna.presentation.ui.insight.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.amplitude.api.Amplitude
import org.cardna.R
import org.cardna.databinding.FragmentOpenAreaBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel

@AndroidEntryPoint
class OpenAreaFragment : BaseViewUtil.BaseFragment<FragmentOpenAreaBinding>(R.layout.fragment_open_area) {
    private val insightViewModel: InsightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.insightViewModel = insightViewModel
        binding.openAreaFragment = this
        initView()
    }

    override fun initView() {
        Amplitude.getInstance().logEvent("Insight_OpenArea")
    }


    fun setArrowClickListener() {
        insightViewModel.setCurrentPosition(InsightFragment.OPEN_AREA)
    }

    fun setCardClickListener() {
        Amplitude.getInstance().logEvent("Insight_OpenArea_DetailPage")
        val intent = Intent(requireContext(), DetailCardActivity::class.java).let {
            it.putExtra(BaseViewUtil.CARD_ID, insightViewModel.openAreaCardId.value)
        }
        startActivity(intent)
    }
}