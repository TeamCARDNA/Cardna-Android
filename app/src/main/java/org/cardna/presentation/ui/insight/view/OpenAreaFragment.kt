package org.cardna.presentation.ui.insight.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentOpenAreaBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.DetailCardActivity
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel
import org.cardna.presentation.util.setSrcWithGlide

@AndroidEntryPoint
class OpenAreaFragment : BaseViewUtil.BaseFragment<FragmentOpenAreaBinding>(R.layout.fragment_open_area) {
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
        binding.ivInsightOpenAreaAllow.setOnClickListener {
            insightViewModel.setCurrentPosition(InsightFragment.OPEN_AREA)
        }
    }

    private fun setCardClickListener() {
        binding.ivInsightOpenAreaImage.setOnClickListener {
            val intent = Intent(requireContext(), DetailCardActivity::class.java).let {
                it.putExtra(BaseViewUtil.CARD_ID, insightViewModel.openAreaCardId.value.toString())
            }
            startActivity(intent)
        }
    }

    private fun setImageObserve() {
        insightViewModel.openAreaInsight.observe(viewLifecycleOwner) { openAreaInsight ->
            requireActivity().setSrcWithGlide(openAreaInsight.imageUrl, binding.ivInsightOpenAreaImage)
        }
    }
}