package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentEditCardDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import org.cardna.data.remote.model.card.RequestEditCardData
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
    }

    private fun initTabLayout() {
        val tabLabel = listOf("카드나", "카드너")
        TabLayoutMediator(
            binding.tlRepresentcardedit,
            binding.rvEditcarddialogContainer
        ) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()
    }

    private fun initAdapter() {
        val fragmentList = listOf(CardMeTabFragment(), CardYouTabFragment())
        editCardTabAdapter = EditCardTabAdapter(this)
        editCardTabAdapter.fragments.addAll(fragmentList)

        binding.rvEditcarddialogContainer.adapter = editCardTabAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun mainCardCount() {
        editCardViewModel.selectedCardList.observe(viewLifecycleOwner) {
            binding.tvRepresentcardeditCardListCount.text = it.size.toString()
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
}
