package org.cardna.presentation.ui.maincard.view


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivityMainCardBinding
import org.cardna.databinding.DialogRelationBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.view.CardCreateActivity
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.viewPagerAnimation
import timber.log.Timber


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
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        initAdapter()
        initData()
        initDialog()
        setClickListener()
    }

    private fun setClickListener() {
        setCardYouWrite()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        val friendId = intent.getIntExtra("friendId", -1)
        val name = intent.getStringExtra("name").plus(getString(R.string.maincard_tv_username_tag))
        binding.mainCardViewModel = mainCardViewModel
        mainCardViewModel.getMainCardList(friendId)
        binding.vpMaincardList.setCurrentItem(mainCardViewModel.cardPosition.value ?: 0, false)
        binding.tvMaincardUserName.text = name
        setInitPagePosition()
    }

    private fun dialogDismiss(dialog: Dialog, relationDialog: DialogRelationBinding) {
        with(relationDialog) {
            clRelationAddFriend.visibility = View.INVISIBLE
            clRelationDisconnect.visibility = View.INVISIBLE
            clRelationProgressingCancel.visibility = View.INVISIBLE
        }
        dialog.dismiss()
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

        //relation 이거를 observe해야함
        val relation = mainCardViewModel.relation.value.toString()
        with(dialogBinding) {
            when (relation) {
                UNKNOWN -> {
                    clRelationAddFriend.visibility = View.VISIBLE
                }
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
            dialogDismiss(dialog, dialogBinding)
        }
    }

    private fun setConfirmDialog(
        dialog: Dialog,
        dialogBinding: DialogRelationBinding,
        friendId: Int
    ) {
        dialogBinding.btnRelationConfirm.setOnClickListener {
            mainCardViewModel.postFriendRequest(friendId)
            mainCardViewModel.getMainCardList(friendId)
            dialogDismiss(dialog, dialogBinding)
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
            viewPagerAnimation(binding.vpMaincardList)
        }
    }

    private fun setDetailActivity() {
        val intent = Intent(this, DetailCardActivity::class.java).apply {
            mainCardViewModel.cardPosition.value?.let {
                mainCardViewModel.cardList.value?.get(it)?.let { card ->
                    putExtra(BaseViewUtil.CARD_ID, card.id)
                }
            }
            startActivity(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCardYouWrite() {
        binding.ivMaincardWrite.setOnClickListener {
            val friendId = intent.getIntExtra("friendId", 0)
            val name = intent.getStringExtra("name")
            val intentCardYou = Intent(this, CardCreateActivity::class.java).apply {
                putExtra("isCardMeOrYou", BaseViewUtil.CARD_YOU)
                putExtra("id", friendId)
                putExtra("name", name)
                putExtra("isCardPackOrMainCard", BaseViewUtil.CARD_YOU)
                startActivity(this)
            }
        }
    }


    companion object {
        const val UNKNOWN = "1.0"
        const val FRIEND = "2.0"
        const val PROGRESSING = "3.0"
    }
}