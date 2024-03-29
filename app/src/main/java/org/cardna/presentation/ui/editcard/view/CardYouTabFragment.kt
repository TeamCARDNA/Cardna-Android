package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import org.cardna.R
import org.cardna.databinding.FragmentCardYouTabBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardDialogAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.util.SpacesItemDecorationCardPack

class CardYouTabFragment :
    BaseViewUtil.BaseFragment<FragmentCardYouTabBinding>(R.layout.fragment_card_you_tab) {
    private lateinit var editCardDialogAdapter: EditCardDialogAdapter
    private val editCardViewModel: EditCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        initData()
        initAdapter()
    }

    private fun initData() {
        editCardViewModel.getCardAll()
    }

    override fun onResume() {
        super.onResume()
        initData()
        editCardViewModel.setCurrentPosition(1)
    }

    private fun initAdapter() {
        editCardDialogAdapter =
            EditCardDialogAdapter(lifecycleOwner = viewLifecycleOwner, editCardViewModel)

        with(binding.rvCardyoutabContainer) {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = editCardDialogAdapter
            addItemDecoration(SpacesItemDecorationCardPack())
        }

        editCardViewModel.cardYouList.observe(viewLifecycleOwner) { it ->
            it.map { it.isMe = false }
            editCardDialogAdapter.apply { submitList(it) }
        }
    }
}