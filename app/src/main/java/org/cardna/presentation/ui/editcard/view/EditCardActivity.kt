package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityEditCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        editCardViewModel.mainCardList.observe(this) {
            editCardAdapter.submitList(it)
        }
        binding.rvRepresentcardeditContainer.layoutManager = GridLayoutManager(this, 2)
        binding.rvRepresentcardeditContainer.adapter = editCardAdapter
    }

    private fun setClickListener() {
        putEditCard()
        startBottomSheetDialog(3)
    }

    private fun startBottomSheetDialog(userId: Int) {
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_edit_card_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        binding.fabRepresentcardedit.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    //이 부분이 문제야
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
}