package org.cardna.presentation.ui.insight.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentBlindAreaBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.DetailCardActivity
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel
import org.cardna.presentation.util.setSrcWithGlide

@AndroidEntryPoint
class BlindAreaFragment : BaseViewUtil.BaseFragment<FragmentBlindAreaBinding>(R.layout.fragment_blind_area) {
    private val insightViewModel: InsightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.insightViewModel = insightViewModel
        initView()
    }

    override fun initView() {
        setImageObserve()
        setArrowClickListener()
        setCardClickListener()
    }

    private fun setArrowClickListener() {
        binding.ivInsightBlindAreaAllow.setOnClickListener {
            insightViewModel.setCurrentPosition(InsightFragment.BLIND_AREA)
        }
    }

    private fun setCardClickListener() {
        binding.ivInsightBlindAreaImage.setOnClickListener {
            val intent = Intent(requireContext(), DetailCardActivity::class.java).let {
                it.putExtra(BaseViewUtil.CARD_ID, insightViewModel.blindAreaCardId.value.toString())
            }
            startActivity(intent)
        }
    }

    private fun setImageObserve() {
        insightViewModel.blindAreaInsight.observe(viewLifecycleOwner) { blindAreaInsight ->
            requireActivity().setSrcWithGlide(blindAreaInsight.imageUrl, binding.ivInsightBlindAreaImage)
        }
    }
}