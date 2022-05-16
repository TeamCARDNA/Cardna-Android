package org.cardna.presentation.ui.editcard.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import org.cardna.R
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.databinding.FragmentEditCardDialogBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardTabAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import timber.log.Timber
import kotlin.math.roundToInt

class EditCardDialogFragment :
    BaseViewUtil.BaseBottomDialogFragment<FragmentEditCardDialogBinding>(R.layout.fragment_edit_card_dialog) {

    private lateinit var editCardTabAdapter: EditCardTabAdapter
    private val editCardViewModel: EditCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editCardViewModel = editCardViewModel
        initView()
    }

    override fun initView() {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.clBottomSheet.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).roundToInt()

        initAdapter()
        initTabLayout()
        mainCardCount()
        putMainCardList()
        changeTabsFont()
    }

    private fun initTabLayout() {
        val tabLabel = listOf("카드나", "카드너")
        TabLayoutMediator(
            binding.tlRepresentcardedit,
            binding.rvEditcarddialogContainer
        ) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()
        binding.tlRepresentcardedit.apply {
            tabRippleColor = null
            layoutParams.height = resources.getDimension(R.dimen.tablayout_view_h).toInt()
        }
    }

    private fun initAdapter() {
        val fragmentList = listOf(CardMeTabFragment(), CardYouTabFragment())
        editCardTabAdapter = EditCardTabAdapter(this)
        editCardTabAdapter.fragments.addAll(fragmentList)

        binding.rvEditcarddialogContainer.adapter = editCardTabAdapter
        binding.tlRepresentcardedit.layoutParams.height =
            resources.getDimension(R.dimen.tablayout_view_h).toInt()
    }

    private fun mainCardCount() {
        binding.tvRepresentcardeditCardListCount.apply {
            editCardViewModel.selectedCardList.observe(viewLifecycleOwner) {
                binding.tvRepresentcardeditCardListCount.text = it.size.toString()
            }
        }
    }

    private fun putMainCardList() {
        binding.tvRepresentcardeditFinish.setOnClickListener {
            with(editCardViewModel) {
                selectedCardList.observe(viewLifecycleOwner) { list ->
                    putEditCard(RequestEditCardData(list))
                }
                mainCardList.observe(viewLifecycleOwner) {
                    val list = it.map { it.id }
                    Timber.d("init submit fragment $list")
                }

            }
            dismiss()
        }
    }

    private fun changeTabsFont() {
        editCardViewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition ->
            if (currentPosition == 0) {
                val vg = binding.tlRepresentcardedit.getChildAt(0) as ViewGroup
                for (j in 0 until 1) {
                    val vgTab = vg.getChildAt(j) as ViewGroup
                    val tabChildsCount = vgTab.childCount
                    for (i in 0 until tabChildsCount) {
                        val tabViewChild = vgTab.getChildAt(i)
                        if (tabViewChild is TextView) {
                            val tf = Typeface.createFromAsset(
                                requireActivity().assets,
                                "pretendard_semibold.ttf"
                            )
                            tabViewChild.typeface = tf
                        }
                    }
                }
                for (j in 1 until 2) {
                    val vgTab = vg.getChildAt(j) as ViewGroup
                    val tabChildsCount = vgTab.childCount
                    for (i in 0 until tabChildsCount) {
                        val tabViewChild = vgTab.getChildAt(i)
                        if (tabViewChild is TextView) {
                            val tf = Typeface.createFromAsset(
                                requireActivity().assets,
                                "pretendard_regular.ttf"
                            )
                            tabViewChild.typeface = tf
                        }
                    }
                }
            } else if (currentPosition == 1) {
                val vg = binding.tlRepresentcardedit.getChildAt(0) as ViewGroup
                for (j in 1 until 2) {
                    val vgTab = vg.getChildAt(j) as ViewGroup
                    val tabChildsCount = vgTab.childCount
                    for (i in 0 until tabChildsCount) {
                        val tabViewChild = vgTab.getChildAt(i)
                        if (tabViewChild is TextView) {
                            val tf = Typeface.createFromAsset(
                                requireActivity().assets,
                                "pretendard_semibold.ttf"
                            )
                            tabViewChild.typeface = tf
                        }
                    }
                }
                for (j in 0 until 1) {
                    val vgTab = vg.getChildAt(j) as ViewGroup
                    val tabChildsCount = vgTab.childCount
                    for (i in 0 until tabChildsCount) {
                        val tabViewChild = vgTab.getChildAt(i)
                        if (tabViewChild is TextView) {
                            val tf = Typeface.createFromAsset(
                                requireActivity().assets,
                                "pretendard_regular.ttf"
                            )
                            tabViewChild.typeface = tf
                        }
                    }
                }
            }
        }
    }

}
