package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import org.cardna.R
import org.cardna.databinding.FragmentCardYouBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackYouRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.SpacesItemDecoration
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class CardYouFragment :
    BaseViewUtil.BaseFragment<FragmentCardYouBinding>(R.layout.fragment_card_you) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardPackViewModel = cardPackViewModel
        initView()
//        initObserve()
    }

    override fun onResume() {
        super.onResume()
        cardPackViewModel.updateCardYouList()  // 카드너 카드들을 서버로부터 불러오기
    }

    override fun initView() {
        initCardYouRvAdapter() // 리사이클러뷰 및 어댑터 설정
        initEmptyViewListener()
        Timber.e("CardYou : ${cardPackViewModel.id}")
        Timber.e("CardYou isCardYouEmpty : ${cardPackViewModel.isCardYouEmpty.value}")
        Timber.e("CardYou isCardMeEmpty : ${cardPackViewModel.isCardMeEmpty.value}")
    }

    private fun initObserve() {
        cardPackViewModel.isCardYouEmpty.observe(viewLifecycleOwner) { it ->
            if (it) { // empty 라면
                Timber.e("CardYou 비어있음")
                binding.ctlCardyouEmpty.visibility = View.VISIBLE
                binding.ctlCardyouNotEmpty.visibility = View.GONE
            } else {
                Timber.e("CardYou 안비어있음")
                binding.ctlCardyouEmpty.visibility = View.GONE
                binding.ctlCardyouNotEmpty.visibility = View.VISIBLE
            }
        }
    }


    // Adapter 생성
    private fun initCardYouRvAdapter() { // CardPack
        var cardYouAdapter =
            CardPackYouRecyclerViewAdapter(cardPackViewModel, viewLifecycleOwner) { // 어댑터 일단 CardPackMeRecyclerViewAdapter로 공유
                // 1. 각 리사이클러뷰 아이템에 달아줄 람다 전달
                Intent(requireContext(), DetailCardActivity::class.java).apply {
                    putExtra(BaseViewUtil.CARD_ID, it.id) // 리사이클러뷰의 아이템 중 카드 선택시 그 카드의 id를 전달
                    startActivity(this)
                }

                // 2. 타인의 카드나일 때는, 공감버튼에 달아줄 리스너 하나 더 전달해줘야 한다.
            }

        with(binding) {
            rvCardyou.adapter = cardYouAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardyou.layoutManager = gridLayoutManager
            rvCardyou.addItemDecoration(
                SpacesItemDecoration
                    ((12 * resources.displayMetrics.density).roundToInt())
            ) // 화면 비율 조정
        }

        // onResume 될 때, cardYouList 를 업데이트 시키고 cardYouList 가 변경되면, 이를 observe 해서 알아서 리사이클러뷰를 갱신해주도록
        cardPackViewModel.cardYouList.observe(viewLifecycleOwner, Observer { it ->
            it?.let { cardYouAdapter.submitList(it) }
        })
    }

    private fun initEmptyViewListener() {
        // 1. 내 카드너 엠티뷰 => 카드너 추가
        binding.ctlBgAddCardyou.setOnClickListener {
            // 카드너 보관함 액티비티로 이동
            val intent = Intent(requireActivity(), CardYouStoreActivity::class.java)
            // 아무것도 안넘겨줘도 됨
            startActivity(intent)
        }

        // 2. 친구 카드너 엠티뷰 => 카드너 작성
        binding.ctlFriendEmptyMakeCardyou.setOnClickListener {
            val intent = Intent(requireActivity(), CardCreateActivity::class.java).apply {
                putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_YOU) // 내 카드나 작성이므로
                putExtra(BaseViewUtil.ID, cardPackViewModel.id.value)
                putExtra(BaseViewUtil.NAME, cardPackViewModel.name)
                putExtra(BaseViewUtil.IS_CARDPACK_OR_MAINCARD, BaseViewUtil.FROM_CARDPACK) // 카드팩에서 왔음을 알려줌
            }
            startActivity(intent)
        }
    }
}