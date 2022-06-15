package org.cardna.presentation.ui.cardpack.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.R
import org.cardna.data.remote.model.card.ResponseCardYouStoreData
import org.cardna.databinding.ActivityCardYouStoreBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.adapter.CardYouStoreRecyclerViewAdapter
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.copyText
import org.cardna.presentation.util.lifeCycled
import org.cardna.presentation.util.shortToast

@AndroidEntryPoint
class CardYouStoreActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityCardYouStoreBinding>(R.layout.activity_card_you_store) {

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
        this.setSystemBarsColor(Color.BLACK, false)
        initData()
        setRvAdapter()
        setCardYouStoreListObserve()
        setClickListener()
        copyMyCodeClickListener()
    }

    private fun setClickListener() {
        codeCopyBtnListener()
    }

    private fun codeCopyBtnListener() {
        binding.llCardyoustoreCopyBtn.setOnClickListener {

            Amplitude.getInstance().logEvent("CardPack_PlusCardner_CodeCopy")
            cardPackViewModel.isMyCode.observe(this) {
                copyText(this@CardYouStoreActivity, it)
            }
        }
    }

    private fun initData() {
        binding.cardpackViewModel = cardPackViewModel
        cardPackViewModel.getCardYouStore()
        cardPackViewModel.getIsMyCode()
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
            it.putExtra(BaseViewUtil.FROM_STORE_KEY, true)
        }
        startActivity(intent)
    }

    private fun copyMyCodeClickListener() {
        binding.llCardyoustoreCopyBtn.setOnClickListener {
            createClipData(binding.tvCardyoustoreCodeText.text.toString())
        }
    }

    private fun createClipData(message: String) {
        val clipBoardManger: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("TAG", message)
        clipBoardManger.setPrimaryClip(clipData)
        this.shortToast("코드가 복사되었습니다")
    }
}