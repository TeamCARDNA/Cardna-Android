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
class CardPackFragment : BaseViewUtil.BaseFragment<FragmentCardPackBinding>(R.layout.fragment_card_pack) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels()
    private lateinit var cardPackTabLayoutAdapter: CardPackTabLayoutAdapter // tabLayout 에 data 띄워주는 adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    override fun onResume() { // 카드팩프래그먼트에서 카드를 눌러 카드 상세페이지로 가서 삭제한다음 왔을 때, 카드팩의 카드들이 업데이트 되어야 하므로 onResume이 필요하다
        super.onResume()
        cardPackViewModel.setTotalCardCnt()
    }


    private fun initViewModel() {
        binding.cardPackViewModel = cardPackViewModel

        if (getArguments() != null) {
            // 이전 Fragment 로부터 id, name 받아옴
            cardPackViewModel.setUserId(getArguments()?.getInt("id", 0))
            cardPackViewModel.setUserName(getArguments()?.getString("name", null))
        }
        // null 이면 본인의 카드팩 접근이므로 id, name 다 초기값인 null 로 있을 것
    }

    override fun initView() {
        initCardPackAdapter()
        initCardPackTabLayout()
        initMeOrFriendCardLayout()
    }


    // 카드나, 카드너 프래그먼트 인스턴스를 생성, tabLayout Adapter 객체 생성 후 fragment 연결, Adapter 를 ViewPager2의 Adapter 로 설정
    private fun initCardPackAdapter() {
        val fragmentList: List<Fragment>
        fragmentList = listOf(
            CardMeFragment(),
            CardYouFragment()
        ) // 그냥 이렇게 인자없이 생성만 해주고, 각 카드나, 카드너 프래그먼트에서는 뷰 모델의 id 값에따라 생성해주면 됨.
        cardPackTabLayoutAdapter = CardPackTabLayoutAdapter(this)
        cardPackTabLayoutAdapter.fragments.addAll(fragmentList)
        binding.vpCardpack.adapter = cardPackTabLayoutAdapter
    }

    // tabLayout 과 viewPager 연결하는 메서드
    private fun initCardPackTabLayout() {
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

    // 유저 본인의 카드팩 프래그먼트인지, 친구의 카드팩 프래그먼트인지에 따라 작업 해주기
    // 리스너 달기, 텍스트뷰, 버튼 등 레이아웃 변화
    private fun initMeOrFriendCardLayout() {
        // 카드팩 총 개수 세팅
        cardPackViewModel.setTotalCardCnt()

        if (cardPackViewModel.id == null) { // 유저 본인의 카드팩 접근 시, 카드추가버튼에 카드나 카드너 추가 바텀씻 올라오는 리스너 달기
            binding.ivAddCard.setOnClickListener {
                (activity as MainActivity).showBottomDialogCardFragment()
            }
        }
    }
}
