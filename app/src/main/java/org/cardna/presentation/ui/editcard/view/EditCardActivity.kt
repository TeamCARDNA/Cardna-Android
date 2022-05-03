package org.cardna.presentation.ui.editcard.view

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.cardna.R
import org.cardna.databinding.ActivityEditCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import org.cardna.presentation.util.*
import timber.log.Timber
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
        StatusBarUtil.setStatusBar(this, Color.BLACK)
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

    private fun setClickListener() {
        putEditCard()
        startBottomSheetDialog()
    }

    //대표카드 리사이클러뷰 어댑터
    private fun initAdapter() {
        editCardAdapter = EditCardAdapter(editCardViewModel)

        with(binding.rvRepresentcardeditContainer) {
            layoutManager = GridLayoutManager(this@EditCardActivity, 2)
            adapter = editCardAdapter
            itemTouchHelperListener(editCardAdapter, this)
            addItemDecoration(SpacesItemDecorationHorizontalCustom())
        }
        editCardViewModel.mainCardList.observe(this) {
            editCardAdapter.submitList(it)
        }
    }

    private fun itemTouchHelperListener(
        editCardAdapter: EditCardAdapter,
        recyclerView: RecyclerView
    ) {
        val itemTouchHelperCallback = ItemTouchHelperCallback(editCardAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(recyclerView)
    }

    private fun startBottomSheetDialog() {
        binding.fabRepresentcardedit.setOnClickListener {
            val bottomSheetDialog =
                EditCardDialogFragment()
            bottomSheetDialog.show(supportFragmentManager, "init bottom_sheet")
        }
    }

    private fun putEditCard() {
        binding.tvTvRepresentcardeditFinish.setOnClickListener {
            val cardIdList = editCardAdapter.mutableList.map { it.id }
            val cardsList = RequestEditCardData(cardIdList)
            Timber.d("ChangeModelEditActivity : $cardIdList")
            editCardViewModel.putEditCard(cardsList)
            editCardViewModel.getChangeSuccess.observe(this) { onSuccess ->
                if (onSuccess) {
                    finish()
                }
            }
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