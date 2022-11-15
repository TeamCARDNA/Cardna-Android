package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentCardMeBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackMeRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.SpacesItemDecorationCardPack
import timber.log.Timber

@AndroidEntryPoint
class CardMeFragment : BaseViewUtil.BaseFragment<FragmentCardMeBinding>(R.layout.fragment_card_me) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // cardPackFragment 의 뷰모델을 공유
    private lateinit var cardMeAdapter: CardPackMeRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardPackViewModel = cardPackViewModel
        initView()
    }

    override fun onResume() {
        super.onResume()
        cardPackViewModel.updateCardMeList()
    }

    override fun initView() {
        initEmptyViewListener()
        initCardMeRvAdapter()
        Timber.e("CardMe isCardMeEmpty : ${cardPackViewModel.isCardMeEmpty.value}")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.e("CardMeFragment onViewStateRestored")
    }

    private fun initCardMeRvAdapter() {
        cardMeAdapter = CardPackMeRecyclerViewAdapter(cardPackViewModel, viewLifecycleOwner) {
            Intent(requireContext(), DetailCardActivity::class.java).apply {
                putExtra(BaseViewUtil.CARD_ID, it.id)
                startActivity(this)
            }
        }

        with(binding) {
            rvCardme.adapter = cardMeAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardme.layoutManager = gridLayoutManager

            rvCardme.addItemDecoration(SpacesItemDecorationCardPack()) // 화면 비율 조정
            rvCardme.layoutManager?.onSaveInstanceState()
            rvCardme.smoothScrollToPosition(0)
        }

        cardPackViewModel.cardMeList.observe(viewLifecycleOwner) { it ->
            it?.let { cardMeAdapter.submitList(it) }
        }
    }

    private fun initEmptyViewListener() {
        binding.ctlBgAddCardme.setOnClickListener {
            Amplitude.getInstance().logEvent("CardPack_Empty_WritingCardna")
            val intent = Intent(requireActivity(), CardCreateActivity::class.java).apply {
                putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_ME)
            }
            startActivity(intent)
        }
    }
}