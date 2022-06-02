package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentCardMeBinding
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
        Timber.e("CardMeFragment onResume")

//        if(cardPackViewModel.cardMeRvPosition != null){
//            binding.rvCardme.layoutManager?.onRestoreInstanceState(cardPackViewModel.cardMeRvPosition)
//        }
        cardPackViewModel.updateCardMeList() // 카드나 카드들을 서버로부터 불러오기
    }

    override fun initView() {
        initEmptyViewListener()
        initCardMeRvAdapter() // 리사이클러뷰 및 어댑터 설정
        Timber.e("CardMe isCardMeEmpty : ${cardPackViewModel.isCardMeEmpty.value}")
    }

    // test를 위한 라이프사이클 콜백 오버라이드 => 나중에 필요 없는 거는 삭제

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.e("CardMeFragment onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
//        binding.rvCardme.smoothScrollToPosition(0) // 이거 적용 시 카드 상세 들어갔다 나와도 맨위로 가서 일단 주석처리
        Timber.e("CardMeFragment onStart")
    }

    override fun onPause() {
        super.onPause()
        Timber.e("CardMeFragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.e("CardMeFragment onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("CardMeFragment onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        saveCardMeRvState()
    }


    // Adapter 생성

    private fun initCardMeRvAdapter() {
        cardMeAdapter = CardPackMeRecyclerViewAdapter(cardPackViewModel, viewLifecycleOwner) { // 어댑터 초기화 =>
            Intent(requireContext(), DetailCardActivity::class.java).apply {
                putExtra(BaseViewUtil.CARD_ID, it.id) // 리사이클러뷰의 아이템 중 카드 선택시 그 카드의 id를 전달
                startActivity(this)
            }
        }

        Timber.e("CardMe : Adapter 생성")

        with(binding) {
            rvCardme.adapter = cardMeAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardme.layoutManager = gridLayoutManager
            rvCardme.addItemDecoration(SpacesItemDecorationCardPack()) // 화면 비율 조정

            rvCardme.layoutManager?.onSaveInstanceState()
            rvCardme.smoothScrollToPosition(0)
        }


        // cardMeList 에 observer 등록
        // onResume 될 때, cardMeList 를 업데이트 시키고 cardMeList 가 변경되면, 이를 observe 해서 알아서 리사이클러뷰를 갱신해주도록
        cardPackViewModel.cardMeList.observe(viewLifecycleOwner) { it ->
            Log.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", it.toString())
            it?.let { cardMeAdapter.submitList(it) }
        }
    }

    private fun initEmptyViewListener() {
        binding.ctlBgAddCardme.setOnClickListener {
            // CardCreateActivity 로 이동
   Amplitude.getInstance().logEvent("CardPack_Empty_WritingCardna")
            val intent = Intent(requireActivity(), CardCreateActivity::class.java).apply {
                putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_ME) // 내 카드나 작성이므로
            }
            startActivity(intent)
        }
    }

    private fun saveCardMeRvState() { // 언제 상태 저장할 것인가
        val state = (binding.rvCardme.layoutManager as GridLayoutManager).onSaveInstanceState()
        cardPackViewModel.setCardMeRvPosition(state!!)
    }

}