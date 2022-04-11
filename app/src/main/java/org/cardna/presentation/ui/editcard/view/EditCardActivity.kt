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

    private fun startBottomSheetDialog() {
        binding.fabRepresentcardedit.setOnClickListener {
            val bottomSheetDialog = EditCardDialogFragment(editCardAdapter.itemCount)
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

    private fun setTextGradient() {
        val text = binding.tvRepresentcardeditColorTitle.text.toString()
        val green = getColor(R.color.main_green)
        val purple = getColor(R.color.main_purple)
        val spannable = text.toSpannable()
        spannable[0..text.length] =
            LinearGradientSpan(text, text, green, purple)
        binding.tvRepresentcardeditColorTitle.text = spannable
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