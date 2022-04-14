package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardYouBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackYouRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.SpacesItemDecoration
import kotlin.math.roundToInt

@AndroidEntryPoint
class CardYouFragment : BaseViewUtil.BaseFragment<FragmentCardYouBinding>(R.layout.fragment_card_you) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        cardPackViewModel.updateCardYouList()
    }

    override fun initView() {
        getCardYou()
        initEmptyViewListener()
    }

    private fun getCardYou(){
        initCardYouRvAdapter() // 리사이클러뷰 및 어댑터 설정
        cardPackViewModel.updateCardYouList() // 카드너 카드들을 서버로부터 불러오기
    }

    // Adapter 생성
    private fun initCardYouRvAdapter(){ // CardPack
        var cardYouAdapter = CardPackYouRecyclerViewAdapter() { // 어댑터 일단 CardPackMeRecyclerViewAdapter로 공유
            // 1. 각 리사이클러뷰 아이템에 달아줄 람다 전달
            val intent = Intent(requireContext(), DetailCardActivity::class.java).apply {
                    putExtra("id", it.id) // 리사이클러뷰의 아이템 중 카드 선택시 그 카드의 id를 전달
                startActivity(this)
            }

            // 2. 타인의 카드나일 때는, 공감버튼에 달아줄 리스너 하나 더 전달해줘야 한다.
        }

        with(binding) {
            rvCardyou.adapter = cardYouAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardyou.layoutManager = gridLayoutManager
            rvCardyou.addItemDecoration(SpacesItemDecoration((12 * resources.displayMetrics.density).roundToInt())) // 화면 비율 조정

            // onResume 될 때, cardMeList 를 업데이트 시키고 cardMeList 가 변경되면, 이를 observe 해서 알아서 리사이클러뷰를 갱신해주도록
            cardPackViewModel?.cardYouList?.observe(viewLifecycleOwner, Observer { it ->
                it?.let { cardYouAdapter.submitList(it)}
            })

            // isCardMeEmpty 에도 옵저버 달아서, true 이면 뷰 바꿔주기
            // 옵저버 달 필요 없이 그냥
            // 카드미 empty 아닌 뷰 ctl, empty 인 뷰 ctl 두개 만들어서
            // 각 2개의 ctl에 setVisibility에 삼항연산자 써서 cardPackViewModel.isCardMeEmpty? View.GONE : View.VISIBLE
            // 해주면 되지 않을까 =>
        }
    }

    private fun initEmptyViewListener(){
        binding.ctlBgAddCardyou.setOnClickListener{
            // 카드너 보관함 액티비티로 이동
            val intent = Intent(requireActivity(), CardCreateActivity::class.java)
            startActivity(intent)
        }
    }
}