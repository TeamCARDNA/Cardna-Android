package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentEditCardDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.cardna.presentation.ui.editcard.adapter.EditCardTabAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardDialogViewModel
import kotlin.math.roundToInt

class EditCardDialogFragment(private val mainCardCount: Int) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditCardDialogBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

    private lateinit var editCardTabAdapter: EditCardTabAdapter
    private val editCardDialogViewModel: EditCardDialogViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_card_dialog, container, false)
        return binding.root
    }

    private fun initView() {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.clBottomSheet.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).roundToInt()

        initData()
        initAdapter()
        initTabLayout()
        mainCardCount()
        setClickListener()
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

    private fun initData() {
        binding.editCardDialogViewModel = editCardDialogViewModel
//        binding.tvRepresentcardeditCardListCount.text = mainCardCount.toString()
        editCardDialogViewModel.getCardAll()
        editCardDialogViewModel.representCardCheck()
    }

    private fun initAdapter() {
        val fragmentList = listOf(CardMeTabFragment(), CardYouTabFragment())
        editCardTabAdapter = EditCardTabAdapter(this)
        editCardTabAdapter.fragments.addAll(fragmentList)
        binding.rvEditcarddialogContainer.adapter = editCardTabAdapter

    }

    private fun mainCardCount() {
        editCardDialogViewModel.selectedCardList.observe(viewLifecycleOwner) {
            binding.tvRepresentcardeditCardListCount.text = it.size.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun setClickListener() {
        binding.tvRepresentcardeditFinish.setOnClickListener {
            dismiss()
        }
    }
}
