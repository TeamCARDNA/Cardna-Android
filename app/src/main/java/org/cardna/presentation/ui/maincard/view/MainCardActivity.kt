package org.cardna.presentation.ui.maincard.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cardna.R
import com.example.cardna.databinding.ActivityMainCardBinding
import com.example.cardna.databinding.DialogRelationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainCardActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainCardBinding>(R.layout.activity_main_card) {
    private val mainCardViewModel: MainCardViewModel by viewModels()
    private lateinit var mainCardAdapter: MainCardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    //메인 프레그먼트 마이페이지 -> 타인꺼보는 것과 똑같은 구조 -> 내가 내껄 볼일은 없음
    override fun initView() {
        initAdapter()
        initData()
        initDialog()
    }

    private fun initData() {
        val friendId = intent.getIntExtra("friendId", -1)
        val name = intent.getStringExtra("name").plus(getString(R.string.maincard_tv_username_tag))
        binding.mainCardViewModel = mainCardViewModel
        binding.vpMaincardList.setCurrentItem(mainCardViewModel.cardPosition.value ?: 0, false)
        binding.tvMaincardUserName.text = name

        mainCardViewModel.getMainCardList(friendId)
        setInitPagePosition()
    }

    private fun initDialog() {
        val dialog = Dialog(this)
        val relationDialog = DialogRelationBinding.inflate(dialog.layoutInflater)
        friendRelationCheck()
        binding.ivMaincardFriend.setOnClickListener {
            initRelationDialog(dialog, relationDialog)
        }
    }

    private fun friendRelationCheck() {
        mainCardViewModel.relation.observe(this) { relation ->
            binding.ivMaincardFriend.apply {
                when (relation.toString()) {
                    UNKNOWN -> setBackgroundResource(R.drawable.ic_mypage_friend_unchecked)
                    FRIEND -> setBackgroundResource(R.drawable.ic_mypage_friend_checked)
                    PROGRESSING -> setBackgroundResource(R.drawable.ic_mypage_friend_ing)
                }
            }
        }
    }

    private fun initRelationDialog(
        dialog: Dialog,
        dialogBinding: DialogRelationBinding,
    ) {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val friendId = intent.getIntExtra("friendId", 0)
        val relation = mainCardViewModel.relation.value.toString()
        with(dialogBinding) {
            when (relation) {
                FRIEND -> {
                    clRelationDisconnect.visibility = View.VISIBLE
                }
                PROGRESSING -> {
                    clRelationProgressingCancel.visibility = View.VISIBLE
                }
            }
            setCancelDialog(dialog, this)
            setConfirmDialog(dialog, this, friendId)
        }
    }

    private fun setCancelDialog(dialog: Dialog, dialogBinding: DialogRelationBinding) {
        dialogBinding.btnRelationCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setConfirmDialog(
        dialog: Dialog,
        dialogBinding: DialogRelationBinding,
        friendId: Int
    ) {
        dialogBinding.btnRelationConfirm.setOnClickListener {
            mainCardViewModel.postFriendRequest(friendId)
            dialog.dismiss()
        }
    }

    private fun setInitPagePosition() {
        binding.vpMaincardList.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mainCardViewModel.saveInitCardPosition(position)
            }
        })
    }

    private fun initAdapter() {
        Timber.d("init adapter")
        mainCardAdapter = MainCardAdapter() {
            setDetailActivity()
        }
        mainCardViewModel.cardList.observe(this) {
            mainCardAdapter.submitList(it)
        }
        with(binding.vpMaincardList) {
            adapter = mainCardAdapter
            viewPagerAnimation()
        }
    }

    private fun setDetailActivity() {
        val intent = Intent(this, DetailCardActivity::class.java).apply {
            mainCardViewModel.cardPosition.value?.let {
                mainCardViewModel.cardList.value?.get(it)?.let {
                    putExtra(BaseViewUtil.CARD_ID, it.id)
                }
            }
            startActivity(this)
        }
    }

    private fun viewPagerAnimation() {
        val compositePageTransformer = getPageTransformer()
        with(binding.vpMaincardList) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            setPageTransformer(compositePageTransformer)
            setPadding(
                (56 * resources.displayMetrics.density).roundToInt(),
                0,
                (56 * resources.displayMetrics.density).roundToInt(),
                0
            )
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun getPageTransformer(): ViewPager2.PageTransformer {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * resources.displayMetrics.density).roundToInt()))

        return compositePageTransformer
    }

    companion object {
        const val UNKNOWN = "1.0"
        const val FRIEND = "2.0"
        const val PROGRESSING = "3.0"
    }
}