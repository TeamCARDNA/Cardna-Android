package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardMeBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackMeRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.SpacesItemDecoration
import kotlin.math.roundToInt

class CardMeFragment : BaseViewUtil.BaseFragment<FragmentCardMeBinding>(R.layout.fragment_card_me) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id,
//  private var isMyCard: Boolean = true // 유저 본인의 카드나에 접근하는건지, 타인의 카드나에 접근하는 건지 이는 나중에

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        cardPackViewModel.updateCardMeList()
    }

    override fun initView() {
        getCardMe()
    }

    // 카드나가 없을 때, 엠티 뷰 처리하기

    private fun getCardMe(){
        initCardMeRvAdapter() // 리사이클러뷰 및 어댑터 설정
        cardPackViewModel.updateCardMeList() // 카드나 카드들을 서버로부터 불러오기

        if(cardPackViewModel.id == null){ // 본인의 카드나 접

        }
        else{ // 타인의 카드나 접근

            // 뷰에 공감버튼 추가해줘야함
        }
    }


    // Adapter 생성
    private fun initCardMeRvAdapter(){
        var cardMeAdapter = CardPackMeRecyclerViewAdapter() { // 어댑터 초기화 =>
            // 1. 각 리사이클러뷰 아이템에 달아줄 람다 전달
            val intent = Intent(requireContext(), DetailCardActivity::class.java).apply {
                putExtra("id", it.id) // 리사이클러뷰의 아이템 중 카드 선택시 그 카드의 id를 전달
                startActivity(this)
            }

            // 2. 타인의 카드나일 때는, 공감버튼에 달아줄 리스너 하나 더 전달해야 된다.
        }

        with(binding) {
            rvCardme.adapter = cardMeAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            rvCardme.layoutManager = gridLayoutManager
            rvCardme.addItemDecoration(SpacesItemDecoration((12 * resources.displayMetrics.density).roundToInt())) // 화면 비율 조정

            // onResume 될 때, cardMeList 를 업데이트 시키고 cardMeList 가 변경되면, 이를 observe 해서 알아서 리사이클러뷰를 갱신해주도록
            cardPackViewModel.cardMeList?.observe(viewLifecycleOwner, Observer { it ->
                it?.let { cardMeAdapter.submitList(it)}
            })
        }
    }
}