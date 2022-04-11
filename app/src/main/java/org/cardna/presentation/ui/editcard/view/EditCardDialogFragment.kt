package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.R
import com.example.cardna.databinding.FragmentEditCardDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import org.cardna.presentation.ui.editcard.adapter.EditCardDialogAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardDialogViewModel
import org.cardna.presentation.util.SpacesItemDecoration
import kotlin.math.roundToInt

class EditCardDialogFragment(private val mainCardCount: Int) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditCardDialogBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

    private lateinit var editCardDialogAdapter: EditCardDialogAdapter
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
    }

    private fun initTabLayout() {
        binding.tlRepresentcardedit.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        editCardDialogViewModel.cardMeList.observe(viewLifecycleOwner) { it ->
                            it.map { it.isMe = true }
                            editCardDialogAdapter.apply { submitList(it) }
                        }
                    }
                    1 -> {
                        editCardDialogViewModel.cardYouList.observe(viewLifecycleOwner) { it ->
                            it.map { it.isMe = false }
                            editCardDialogAdapter.apply { submitList(it) }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun initData() {
        binding.editCardDialogViewModel = editCardDialogViewModel
        binding.tvRepresentcardeditCardListCount.text = mainCardCount.toString()
        editCardDialogViewModel.getCardAll()
    }

    private fun initAdapter() {
        editCardDialogAdapter = EditCardDialogAdapter()
        editCardDialogViewModel.cardMeList.observe(viewLifecycleOwner) {
            it.map { it.isMe = true }
            editCardDialogAdapter.submitList(it)
        }

        with(binding.rvEditcarddialogContainer) {
            this.adapter = editCardDialogAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
            addItemDecoration(SpacesItemDecoration((12 * resources.displayMetrics.density).roundToInt()))
        }
    }

    private fun mainCardCount() {
        editCardDialogAdapter.apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    super.onItemRangeRemoved(positionStart, itemCount)
                    binding.tvRepresentcardeditCardListCount.text =
                        editCardDialogAdapter.itemCount.toString()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
}