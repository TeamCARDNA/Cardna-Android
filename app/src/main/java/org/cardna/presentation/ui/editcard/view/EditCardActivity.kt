package org.cardna.presentation.ui.editcard.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amplitude.api.Amplitude
import org.cardna.R
import org.cardna.databinding.ActivityEditCardBinding
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.editcard.adapter.EditCardAdapter
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
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

    //대표카드 리사이클러뷰 어댑터
    private fun initAdapter() {
        editCardAdapter = EditCardAdapter(editCardViewModel)

        with(binding.rvRepresentcardeditContainer) {
            layoutManager = GridLayoutManager(this@EditCardActivity, 2)
            adapter = editCardAdapter
            addItemDecoration(SpacesItemDecorationHorizontalDialog())
            itemTouchHelperListener(editCardAdapter, this)
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
            Amplitude.getInstance().logEvent("MainCardEdit_Cardpack")
            val bottomSheetDialog =
                EditCardDialogFragment()
            editCardViewModel.mainCardList.observe(this) {
                if (it.isNullOrEmpty())
                    editCardViewModel.setChangeSelectedList(mutableListOf<Int>())
            }
            bottomSheetDialog.show(supportFragmentManager, "init bottom_sheet")

        }
    }

    private fun putEditCard() {
        binding.tvTvRepresentcardeditFinish.setOnClickListener {
            Amplitude.getInstance().logEvent("MainCardEdit_Finish ")
            val cardsList = RequestEditCardData(editCardAdapter.mutableList.map { it.id })
            Timber.d("list- put : $cardsList")
            editCardViewModel.putEditCard(cardsList)
            editCardViewModel.isSuccess.observe(this) {
                if (it) {
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