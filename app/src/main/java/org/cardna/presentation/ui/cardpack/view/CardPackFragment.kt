package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.cardna.R
import com.example.cardna.databinding.CardpackCustomTablayoutBinding
import com.example.cardna.databinding.FragmentCardPackBinding
import com.example.cardna.databinding.FragmentInsightBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackTabLayoutAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel

@AndroidEntryPoint
class CardPackFragment :
    BaseViewUtil.BaseFragment<FragmentCardPackBinding>(R.layout.fragment_card_pack) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels()
    private lateinit var cardPackTabLayoutAdapter: CardPackTabLayoutAdapter // tabLayout 에 data 띄워주는 adapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initViewModel() {
        binding.cardPackViewModel = cardPackViewModel
        binding.lifecycleOwner = this@CardPackFragment

        if (getArguments() != null) { // 로직 변경 필요
            cardPackViewModel.setUserId(getArguments()?.getInt("id", 0))
            cardPackViewModel.setUserName(getArguments()?.getString("name", null))
        }
    }

    override fun initView() {
        initCardPackAdapter()
        initCardPackTabLayout()
    }


    // 카드나, 카드너 프래그먼트 인스턴스를 생성해주고 tabLayout Adapter 객체 생성해서 거기에 fragment 들 연결하고 그 Adapter 를 ViewPager2의 Adapter 로 설정
    private fun initCardPackAdapter() { // onResume()에 이거 넣어줘도 될듯 ? 애초에 onResume이 필요 ?
        val fragmentList: List<Fragment>

        fragmentList = listOf(
            CardMeFragment(),
            CardYouFragment()
        ) // 그냥 이렇게 해주고, 각 카드나, 카드너 프래그먼트에서는 뷰 모델의 id 값에따라 생성해주면 됨.
        initMeOrFriendCardLayout() // 이걸 따로 빼서 그냥 initView() 에다 넣으면 안되나
        cardPackTabLayoutAdapter = CardPackTabLayoutAdapter(this)
        cardPackTabLayoutAdapter.fragments.addAll(fragmentList)
        binding.vpCardpack.adapter = cardPackTabLayoutAdapter
    }

    private fun initCardPackTabLayout() { // tabLayout 과 viewPager 연결하는 메서드
        val tabLabel = listOf("카드나", "카드너")

        TabLayoutMediator(binding.tlCardpack, binding.vpCardpack) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()

        binding.tlCardpack.getTabAt(0)?.customView = createTabLayout("카드나")
        binding.tlCardpack.getTabAt(1)?.customView = createTabLayout("카드너")

        for (position: Int in 0..binding.tlCardpack.tabCount) {
            if (binding.tlCardpack.getTabAt(position) != null) {
                binding.tlCardpack.setPaddingRelative(0, 0, 0, 0)
            }
        }
    }

    private fun createTabLayout(tabName: String): View { // 각 탭 레이아웃에 탭의 뷰 디자인 하는 메서드
        val tabBinding: CardpackCustomTablayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.cardpack_custom_tablayout,
            null,
            false
        )

        with(tabBinding) {
            when (tabName) {
                "카드나" -> {
                    tvCardmeTab.text = tabName
                    isCardme = true
                    ivCardpackTab.setImageResource(R.drawable.ic_selector_cardpack_tab_cardme)
                    viewCardpackLine.setBackgroundResource(R.drawable.ic_selector_cardpack_tab_cardme_line)
                }

                "카드너" -> {
                    tvCardyouTab.text = tabName
                    isCardme = false
                    ivCardpackTab.setImageResource(R.drawable.ic_selector_cardpack_tab_cardyou)
                    viewCardpackLine.setBackgroundResource(R.drawable.ic_selector_cardpack_tab_cardyou_line)
                }
            }
        }
        return tabBinding.root
    }


    private fun initMeOrFriendCardLayout() {
        // 유저 본인의 카드팩 프래그먼트인지, 친구의 카드팩 프래그먼트인지에 따라서
        // 카드팩 프래그먼트 버튼 등 레이아웃 변화
        // 카드팩 총 개수 세팅
        if (cardPackViewModel.id == null) { // 유저 본인의 카드팩 접근
            lifecycleScope.launch {

                // totalCardCnt ViewModel 의 프로퍼티로 두고, 이 메서드도 ViewModel 안에 정의하고, 레이아웃의 tvCardpackCnt 에
                // 이 프로퍼티 결합하면 자동 업데이트 되지 않나 ?

//                val totalCardCnt = ApiService.cardService.getCardMe().data.totalCardCnt
//                withContext(Dispatchers.Main) {
//                    binding.tvCardpackCnt.text = totalCardCnt.toString()
//                }
            }
            setMakeCardIvListener()
        } else { // 친구의 카드팩 접근
            with(binding) {
                // 친구의 카드팩이면 그냥 카드팩 텍스트뷰,
                // "카드팩" 텍스트뷰, 카드 총 개수, 카드 작성 버튼이 담겨있는 ctl_cardpack_top  => invisible
                // FriendCardPackActivity에서 xx님의 카드팩, 카드너작성버튼 추가 시키면 될듯
                ctlCardpackTop.visibility = View.GONE // 이러면 자식뷰 다 GONE 되나 ?
            }
        }
    }

    // 오른쪽 상단 카드나, 카드너 추가 버튼에 리스너 다는 함수 => 이 함수는 xml 상에서 onClick 으로 넣어줘도 될 듯 ?
    // 이를 클릭하면 MainActivity의 함수를 실행시켜 줘야함
    private fun setMakeCardIvListener() {
        binding.ivAddCard.setOnClickListener {
            (activity as MainActivity).showBottomDialogCardFragment()
        }
    }

}
