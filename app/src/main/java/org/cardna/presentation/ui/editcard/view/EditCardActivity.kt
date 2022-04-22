package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.R
import com.example.cardna.databinding.ActivityEditCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.util.LinearGradientSpan
import org.cardna.presentation.util.SpacesItemDecoration2
import org.cardna.presentation.util.setGradientText
import kotlin.math.roundToInt

@AndroidEntryPoint
class EditCardActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityEditCardBinding>(R.layout.activity_edit_card) {
    private val editCardViewModel: EditCardViewModel by viewModels()
    private lateinit var editCardAdapter: EditCardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initData()
        initAdapter()
        setClickListener()
        setTextGradient()
        mainCardCount()
    }

    private fun initData() {
        binding.editCardViewModel = editCardViewModel
        editCardViewModel.getMainCard()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initAdapter() {
        editCardAdapter = EditCardAdapter()
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

    // 플로팅 버튼 listener -> bottomSheetDialog 띄워줌
    private fun startBottomSheetDialog() {
        binding.fabRepresentcardedit.setOnClickListener {
            val bottomSheetDialog = EditCardDialogFragment(editCardAdapter.itemCount)
            bottomSheetDialog.show(supportFragmentManager, "init bottom_sheet")
        }
    }

    //대표카드 수정 완료처리
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

    //대표카드 수정 activity 에 있는 item 개수
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