package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityTempCardYouStoreBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.data.remote.model.card.ResponseCardYouStoreData
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardYouStoreRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.lifeCycled

@AndroidEntryPoint
class TempCardYouStoreActivity : BaseViewUtil.BaseAppCompatActivity<ActivityTempCardYouStoreBinding>(R.layout.activity_temp_card_you_store) {

    private val cardPackViewModel: CardPackViewModel by viewModels()

    private val cardYouStoreAdapter: CardYouStoreRecyclerViewAdapter by lifeCycled {
        CardYouStoreRecyclerViewAdapter { gotoDetailCardActivity(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        initData()
        setRvAdapter()
        setCardYouStoreListObserve()
    }

    private fun initData() {
        cardPackViewModel.getCardYouStore()
    }

    private fun setRvAdapter() {
        binding.rvCardyoustore.layoutManager = LinearLayoutManager(this)
        binding.rvCardyoustore.adapter = cardYouStoreAdapter
    }

    private fun setCardYouStoreListObserve() {
        cardPackViewModel.cardYouStoreList.observe(this) {
            cardYouStoreAdapter.submitList(it)
        }
    }

    private fun gotoDetailCardActivity(data: ResponseCardYouStoreData.Data) {
        val intent = Intent(this, DetailCardActivity::class.java).let {
            it.putExtra(BaseViewUtil.CARD_ID, data.id)
        }
        startActivity(intent)
    }
}