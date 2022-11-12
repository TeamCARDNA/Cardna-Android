package org.cardna.presentation.ui.cardpack.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.amplitude.api.Amplitude
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.databinding.CardpackCustomTablayoutBinding
import org.cardna.databinding.FragmentCardPackBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardPackTabLayoutAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel


@AndroidEntryPoint
class CardPackFragment :
    BaseViewUtil.BaseFragment<FragmentCardPackBinding>(org.cardna.R.layout.fragment_card_pack) {
    private val cardPackViewModel: CardPackViewModel by activityViewModels()
    private lateinit var cardPackTabLayoutAdapter: CardPackTabLayoutAdapter // tabLayout 에 data 띄워주는 adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Amplitude.getInstance().logEvent("CardPack")
        initViewModel()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (cardPackViewModel.id.value == null)
            cardPackViewModel.setTotalCardCnt()
        binding.vpCardpack.setCurrentItem(cardPackViewModel.tabPosition.value ?: 0, false)
    }

    private fun initViewModel() {
        binding.cardPackViewModel = cardPackViewModel
    }

    override fun initView() {
        initCardPackAdapter()
        initCardPackTabLayout()
        initMeOrFriendCardLayout()
        setInitPagePosition()
        binding.vpCardpack.setCurrentItem(cardPackViewModel.tabPosition.value ?: 0, false)
    }

    private fun initCardPackAdapter() {
        val fragmentList: List<Fragment>
        fragmentList = listOf(
            CardMeFragment(),
            CardYouFragment()
        )
        cardPackTabLayoutAdapter = CardPackTabLayoutAdapter(this)
        cardPackTabLayoutAdapter.fragments.addAll(fragmentList)
        binding.vpCardpack.adapter = cardPackTabLayoutAdapter
    }

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
            org.cardna.R.layout.cardpack_custom_tablayout,
            null,
            false
        )

        with(tabBinding) {
            when (tabName) {
                "카드나" -> {
                    tvCardmeTab.text = tabName
                    isCardme = true
                    ivCardpackTab.setImageResource(org.cardna.R.drawable.ic_selector_cardpack_tab_cardme)
                    viewCardpackLine.setBackgroundResource(org.cardna.R.drawable.ic_selector_cardpack_tab_cardme_line)
                }

                "카드너" -> {
                    tvCardyouTab.text = tabName
                    isCardme = false
                    ivCardpackTab.setImageResource(org.cardna.R.drawable.ic_selector_cardpack_tab_cardyou)
                    viewCardpackLine.setBackgroundResource(org.cardna.R.drawable.ic_selector_cardpack_tab_cardyou_line)
                }
            }
        }
        return tabBinding.root
    }

    private fun initMeOrFriendCardLayout() {
        if (cardPackViewModel.id.value == null) {
            cardPackViewModel.setTotalCardCnt()

            binding.ctlAddCardBg.setOnClickListener {
                binding.ctlAddCardBg.isClickable = false
                (activity as MainActivity).showBottomDialogCardFragment()

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                        binding.ctlAddCardBg.isClickable = true
                    },1000
                )
            }
        }
    }

    private fun setInitPagePosition() {
        binding.vpCardpack.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                cardPackViewModel.saveInitTabPosition(position)
            }
        })
    }
}
