package org.cardna.presentation.ui.editcard.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.R
import com.example.cardna.databinding.ActivityEditCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardDialogViewModel
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.setGradientText

@AndroidEntryPoint
class EditCardActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityEditCardBinding>(R.layout.activity_edit_card) {
    private val editCardViewModel: EditCardViewModel by viewModels()
    private val editCardDialogViewModel: EditCardDialogViewModel by viewModels()

    private lateinit var editCardAdapter: EditCardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.editCardViewModel = editCardViewModel
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        initData()
        initAdapter()
        setClickListener()
        setTextGradient()
        mainCardCount()
    }

    private fun initData() {
        editCardViewModel.getMainCard()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    //대표카드 리사이클러뷰 어댑터
    private fun initAdapter() {
        editCardAdapter = EditCardAdapter(editCardDialogViewModel)
        with(binding.rvRepresentcardeditContainer) {
            layoutManager = GridLayoutManager(this@EditCardActivity, 2)
            adapter = editCardAdapter

//            val itemTouchHelperCallback = ItemTouchHelperCallback(editCardAdapter)
//            val helper = ItemTouchHelper(itemTouchHelperCallback)
//            helper.attachToRecyclerView(this)
        }

        editCardViewModel.mainCardList.observe(this) {
            editCardAdapter.submitList(it)
        }
    }

    private fun setClickListener() {

        putEditCard()
        startBottomSheetDialog()
    }

    private fun startBottomSheetDialog() {
        binding.fabRepresentcardedit.setOnClickListener {
            var mainCardList: List<Int> = emptyList()
            editCardViewModel.mainCardList.observe(this) { list ->
                mainCardList = list.map { it.id }
            }
            val bottomSheetDialog =
                EditCardDialogFragment(mainCardList)
            bottomSheetDialog.show(supportFragmentManager, "init bottom_sheet")
        }
    }

    private fun putEditCard() {
        binding.tvTvRepresentcardeditFinish.setOnClickListener {
            val cardsList = RequestEditCardData(editCardAdapter.currentList.map { it.id })
            editCardViewModel.putEditCard(cardsList)
            finish()
        }
    }

    // text Gradient
    private fun setTextGradient() {
        val text = binding.tvRepresentcardeditColorTitle.text.toString()
        binding.tvRepresentcardeditColorTitle.text = setGradientText(text)
    }

    private fun mainCardCount() {
        editCardAdapter.apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    super.onItemRangeRemoved(positionStart, itemCount)
                    binding.tvEditcardCount.text = editCardAdapter.itemCount.toString()
                }
            })
        }
    }
}