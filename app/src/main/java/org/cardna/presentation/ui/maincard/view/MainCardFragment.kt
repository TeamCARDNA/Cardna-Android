package org.cardna.presentation.ui.maincard.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cardna.R
import com.example.cardna.databinding.DialogMainCardBlockBinding
import com.example.cardna.databinding.DialogRelationBinding
import com.example.cardna.databinding.FragmentMainCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.editcard.view.EditCardActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import org.cardna.presentation.util.setGradientText
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainCardFragment :
    BaseViewUtil.BaseFragment<FragmentMainCardBinding>(R.layout.fragment_main_card) {
    private lateinit var mainCardAdapter: MainCardAdapter
    private val mainCardViewModel: MainCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun initView() {
        initData()
        initAdapter()
        initDialog()
        setClickListener()
        checkUserId()
    }

    override fun onResume() {
        super.onResume()
        initData()
//        checkUserId()
    }

    //뿌려질 데이터
    private fun initData() {
        binding.mainCardViewModel = mainCardViewModel
        setInitPagePosition()
        binding.vpMaincardList.setCurrentItem(mainCardViewModel.cardPosition.value ?: 0, false)
    }

    private fun checkUserId() {
        var id = -1
        if (arguments != null) {
            val name = arguments?.getString("name")
            id = arguments?.getInt("id", -1) ?: -1
            mainCardViewModel.getMyPageUser(name!!)
            setFriendIcon()
        } else {
            mainCardViewModel.getMyPageUser()
        }
        mainCardViewModel.getMainCardList(id)
    }

    private fun setFriendIcon() {
        mainCardViewModel.relation.observe(viewLifecycleOwner) {
            with(binding.ivMaincardFriend) {
                when (it.toString()) {
                    UNKNOWN -> setBackgroundResource(R.drawable.ic_mypage_friend_unchecked)
                    FRIEND -> {
                        setBackgroundResource(R.drawable.ic_mypage_friend_checked)
                        binding.tvMaincardGotoCardpack.apply {
                            this.text = requireActivity().setGradientText(this.text.toString())
                        }
                    }
                    PROGRESSING -> setBackgroundResource(R.drawable.ic_mypage_friend_ing)
                    else -> setBackgroundResource(R.drawable.ic_alarm)
                }
            }
        }
    }

    //adapter 관련 모음
    private fun initAdapter() {
        Timber.d("init adapter")
        mainCardAdapter = MainCardAdapter() {
            setDetailActivity()
        }
        mainCardViewModel.cardList.observe(viewLifecycleOwner) {
            mainCardAdapter.submitList(it)
        }
        with(binding.vpMaincardList) {
            adapter = mainCardAdapter
            viewPagerAnimation()
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

    //click listener
    private fun setClickListener() {
        setEditCardActivity()
        setAlarmActivity()
        setCardYouWrite()
    }

    private fun setCardYouWrite() {
        binding.ivMaincardWrite.setOnClickListener {
            requireActivity().shortToast("카드너 작성 뷰 이동")
        }
    }


    private fun setDetailActivity() {
        val intent = Intent(requireActivity(), DetailCardActivity::class.java).apply {
            mainCardViewModel.cardPosition.value?.let {
                mainCardViewModel.cardList.value?.get(it)?.let {
                    putExtra(BaseViewUtil.CARD_ID, it.id)
                }
            }
            startActivity(this)
        }
    }

    private fun setEditCardActivity() {
        binding.llMaincardEditLayout.setOnClickListener {
            val intent = Intent(requireActivity(), EditCardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAlarmActivity() {
        binding.ibtnMaincardAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), AlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDialog() {
        val dialog = Dialog(requireActivity())
        val relationDialog = DialogRelationBinding.inflate(dialog.layoutInflater)
        val blockDialog = DialogMainCardBlockBinding.inflate(dialog.layoutInflater)

        val isBlock = mainCardViewModel.isBlocked.value

        userBlockCheck(isBlock, dialog, blockDialog)
        binding.ivMaincardFriend.setOnClickListener {
            initRelationDialog(dialog, relationDialog)
        }
    }


    private fun initRelationDialog(
        dialog: Dialog,
        dialogBinding: DialogRelationBinding
    ) {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val relation = mainCardViewModel.relation.value.toString()
        val friendId = arguments?.getInt("id", 0) ?: -1
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
            setConfirmDialog(this, friendId)
        }

    }

    private fun setCancelDialog(dialog: Dialog, dialogBinding: DialogRelationBinding) {
        dialogBinding.btnRelationCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setConfirmDialog(
        dialogBinding: DialogRelationBinding,
        friendId: Int
    ) {
        dialogBinding.btnRelationConfirm.setOnClickListener {
            requireActivity().shortToast("친구 손절")
            mainCardViewModel.postFriendRequest(friendId)
        }
    }

    private fun userBlockCheck(
        isBlock: Boolean?,
        dialog: Dialog,
        dialogBinding: DialogMainCardBlockBinding
    ) {
        if (isBlock == true) {
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()
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

    companion object {
        const val UNKNOWN = "1.0"
        const val FRIEND = "2.0"
        const val PROGRESSING = "3.0"
    }
}
