package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardMeTabBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardDialogAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.util.SpacesItemDecoration
import kotlin.math.roundToInt

class CardMeTabFragment :
    BaseViewUtil.BaseFragment<FragmentCardMeTabBinding>(R.layout.fragment_card_me_tab) {
    private lateinit var editCardDialogAdapter: EditCardDialogAdapter
    private val editCardViewModel: EditCardViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun initView() {
        initData()
        initAdapter()
    }

    private fun initData() {
        editCardViewModel.getCardAll()
    }

    private fun initAdapter() {
        editCardDialogAdapter =
            EditCardDialogAdapter(lifecycleOwner = viewLifecycleOwner, editCardViewModel)
        editCardViewModel.cardMeList.observe(viewLifecycleOwner) { it ->
            it.map { it.isMe = true }
            editCardDialogAdapter.apply { submitList(it) }
        }

        with(binding.rvCardmetabContainer) {
            this.adapter = editCardDialogAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
            addItemDecoration(SpacesItemDecoration((12 * resources.displayMetrics.density).roundToInt()))
        }
    }

    private fun selectedCardMeItem() {

    }
}

