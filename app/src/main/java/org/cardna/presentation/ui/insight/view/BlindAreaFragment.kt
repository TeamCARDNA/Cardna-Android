package org.cardna.presentation.ui.insight.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.amplitude.api.Amplitude
import org.cardna.R
import org.cardna.databinding.FragmentBlindAreaBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel

@AndroidEntryPoint
class BlindAreaFragment : BaseViewUtil.BaseFragment<FragmentBlindAreaBinding>(R.layout.fragment_blind_area) {
    private val insightViewModel: InsightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.insightViewModel = insightViewModel
        binding.blindAreaFragment = this
        initView()
    }

    override fun initView() {
        Amplitude.getInstance().logEvent("Insight_BlindArea")

    }

    fun setArrowClickListener() {
        insightViewModel.setCurrentPosition(InsightFragment.BLIND_AREA)
    }

    fun setCardClickListener() {
        Amplitude.getInstance().logEvent("Insight_BlindArea_DetailPage")
        val intent = Intent(requireContext(), DetailCardActivity::class.java).let {
            it.putExtra(BaseViewUtil.CARD_ID, insightViewModel.blindAreaCardId.value)
        }
        startActivity(intent)
    }
}