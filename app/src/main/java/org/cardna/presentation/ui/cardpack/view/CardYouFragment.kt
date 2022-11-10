package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentCardYouBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackYouRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.SpacesItemDecorationCardPack

@AndroidEntryPoint
class CardYouFragment :
    BaseViewUtil.BaseFragment<FragmentCardYouBinding>(R.layout.fragment_card_you) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardPackViewModel = cardPackViewModel
        initView()
    }

    override fun onResume() {
        super.onResume()
        Amplitude.getInstance().logEvent("CardPack_Cardner ")
        cardPackViewModel.updateCardYouList()
    }

    override fun initView() {
        initCardYouRvAdapter() // 리사이클러뷰 및 어댑터 설정
        initEmptyViewListener()
    }

    private fun initCardYouRvAdapter() {
        var cardYouAdapter =
            CardPackYouRecyclerViewAdapter(
                cardPackViewModel,
                viewLifecycleOwner
            ) {
                Intent(requireContext(), DetailCardActivity::class.java).apply {
                    putExtra(BaseViewUtil.CARD_ID, it.id)
                    startActivity(this)
                }
            }

        with(binding) {
            rvCardyou.adapter = cardYouAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardyou.layoutManager = gridLayoutManager
            rvCardyou.addItemDecoration(
                SpacesItemDecorationCardPack()
            )
        }

        cardPackViewModel.cardYouList.observe(viewLifecycleOwner, Observer { it ->
            it?.let { cardYouAdapter.submitList(it) }
        })
    }

    private fun initEmptyViewListener() {
        binding.ctlBgAddCardyou.setOnClickListener {
            Amplitude.getInstance().logEvent("CardPack_Empty_PlusCardner")
            val intent = Intent(requireActivity(), CardYouStoreActivity::class.java)
            startActivity(intent)
        }

        binding.ctlFriendEmptyMakeCardyou.setOnClickListener {
            val intent = Intent(requireActivity(), CardCreateActivity::class.java).apply {
                putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_YOU)
                putExtra(BaseViewUtil.ID, cardPackViewModel.id.value)
                putExtra(BaseViewUtil.NAME, cardPackViewModel.name)
                putExtra(
                    BaseViewUtil.IS_CARDPACK_OR_MAINCARD,
                    BaseViewUtil.FROM_CARDPACK
                )
            }
            startActivity(intent)
        }
    }
}