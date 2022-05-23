package org.cardna.presentation.ui.maincard.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.DialogMainCardBlockBinding
import org.cardna.databinding.DialogRelationBinding
import org.cardna.databinding.FragmentMainCardBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.cardpack.view.CardCreateActivity
import org.cardna.presentation.ui.cardpack.view.FriendCardPackActivity
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.editcard.view.EditCardActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
import org.cardna.presentation.util.getToast
import org.cardna.presentation.util.setGradientText
import org.cardna.presentation.util.shortToast
import org.cardna.presentation.util.viewPagerAnimation
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainCardFragment :
    BaseViewUtil.BaseFragment<FragmentMainCardBinding>(R.layout.fragment_main_card) {
    private lateinit var mainCardAdapter: MainCardAdapter
    private val mainCardViewModel: MainCardViewModel by activityViewModels()
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("bottomtest MainCardFragment onCreate")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("bottomtest MainCardFragment onViewCreated")
        initView()
    }

    override fun initView() {
        initData()
        initAdapter()
        initDialog()
        setClickListener()
        checkUserId()
        setTextGradient()
    }

    private fun setTextGradient() {
        mainCardViewModel.relation.observe(viewLifecycleOwner) {
            if (it.toString() == MainCardActivity.FRIEND)
                binding.tvMaincardGotoCardpack.apply {
                    this.text = requireActivity().setGradientText(this.text.toString())
                }
        }
    }

    override fun onResume() {
        Timber.e("bottomtest MainCardFragment onResume")
        super.onResume()

        initData()
        checkUserId()
    }

    private fun setAlarmExist() {
        mainCardViewModel.setAlarmExist()
    }

    //뿌려질 데이터
    private fun initData() {
        setAlarmExist()
        mainCardViewModel.isAlarmExist.observe(viewLifecycleOwner) {
            if (it == false && CardNaRepository.alarmExistCount < mainCardViewModel.updateAlarmCount.value!!) binding.icAlarmStatus.visibility =
                View.VISIBLE
            else binding.icAlarmStatus.visibility = View.INVISIBLE

        }
        binding.mainCardViewModel = mainCardViewModel
        setInitPagePosition()

        binding.vpMaincardList.setCurrentItem(mainCardViewModel.cardPosition.value ?: 0, false)
    }

    //click listener
    private fun setClickListener() {
        setEditCardActivity()
        setAlarmActivity()
        setCardYouWrite()
        setGotoFriendCardPack()
    }

    private fun checkUserId() {
        var id = -1
        if (arguments != null) {
            with(binding) {
                llMaincardEditLayout.visibility = View.GONE
                clMaincardAlarm.visibility =
                    View.INVISIBLE  //TODO 뷰갱신될때 너무 깜빡여서 API통신전 처리하려고 다빈이 추가
                llMaincardMypageIconContainer.visibility =
                    View.VISIBLE  //TODO 뷰갱신될대 너무 깜빡여서 API통신전 처리하려고 다빈이 추가
                ivMaincardGotoCardpackBackground.visibility = View.VISIBLE
            }

            val name = arguments?.getString("name")
            id = arguments?.getInt("id", -1) ?: -1
            mainCardViewModel.getMyPageUser(name!!)
            mainCardViewModel.setFriendNameAndId(name, id)
            setFriendIcon()
        } else {
            with(binding) {
                ivMaincardGotoCardpackBackground.visibility = View.INVISIBLE
                llMaincardMypageIconContainer.visibility = View.INVISIBLE
                clMaincardAlarm.visibility = View.VISIBLE
                llMaincardEditLayout.visibility = View.VISIBLE
            }

            mainCardViewModel.getMyPageUser()
        }
        mainCardViewModel.getMainCardList(id)
    }

    private fun setFriendIcon() {
        mainCardViewModel.relation.observe(viewLifecycleOwner) {
            with(binding.ivMaincardFriend) {
                when (it.toString()) {
                    MainCardActivity.UNKNOWN -> setBackgroundResource(R.drawable.ic_mypage_friend_unchecked)
                    MainCardActivity.FRIEND -> {
                        setBackgroundResource(R.drawable.ic_mypage_friend_checked)
                    }
                    MainCardActivity.REQUEST,
                    MainCardActivity.RESPONSE -> setBackgroundResource(R.drawable.ic_mypage_friend_ing)
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
            requireActivity().viewPagerAnimation(binding.vpMaincardList)
        }
    }

    private fun setCardYouWrite() {
        binding.ivMaincardWrite.setOnClickListener {
            val friendId = arguments?.getInt(BaseViewUtil.ID, -1)
            val name = arguments?.getString("name")

            Intent(requireActivity(), CardCreateActivity::class.java).apply {
                putExtra("isCardMeOrYou", BaseViewUtil.CARD_YOU)
                putExtra(BaseViewUtil.ID, friendId)
                putExtra("name", name)
                putExtra("isCardPackOrMainCard", BaseViewUtil.CARD_YOU)
                startActivity(this)
            }
        }
    }

    private fun setDetailActivity() {
        Intent(requireActivity(), DetailCardActivity::class.java).apply {
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
        binding.clMaincardAlarm.setOnClickListener {
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
        dialogBinding: DialogRelationBinding,
    ) {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val friendId = arguments?.getInt("id", 0) ?: -1
        //relation 이거를 observe해야함 -> 내 상태
        val relation = mainCardViewModel.relation.value.toString()
        val code = myPageViewModel.myPage.value?.code
        Timber.d("code : $code")
        with(dialogBinding) {
            when (relation) {
                MainCardActivity.UNKNOWN -> {
                    clRelationAddFriend.visibility = View.VISIBLE
                }
                MainCardActivity.FRIEND -> {
                    clRelationDisconnect.visibility = View.VISIBLE
                }
                MainCardActivity.RESPONSE, MainCardActivity.REQUEST -> {
                    clRelationProgressingCancel.visibility = View.VISIBLE
                }
            }
            setConfirmDialog(dialog, dialogBinding, friendId)
            setCancelDialog(dialog, dialogBinding)
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


    private fun dialogDismiss(dialog: Dialog, relationDialog: DialogRelationBinding) {
        with(relationDialog) {
            clRelationAddFriend.visibility = View.INVISIBLE
            clRelationDisconnect.visibility = View.INVISIBLE
            clRelationProgressingCancel.visibility = View.INVISIBLE
        }
        dialog.dismiss()
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

    private fun setGotoFriendCardPack() {
        binding.ivMaincardGotoCardpackBackground.setOnClickListener {
            startActivity(
                Intent(requireContext(), FriendCardPackActivity::class.java)
                    .putExtra(BaseViewUtil.ID, mainCardViewModel.friendId.value)
                    .putExtra(BaseViewUtil.NAME, mainCardViewModel.friendName.value)
            )
        }
    }

    //TODO 나 다빈인데 마이페이지랑 연관된 로직이 필요해서 적어뒀엉 지우지 마라조~
    override fun onDestroyView() {
     myPageViewModel.settingBtnIsValid(true)
   myPageViewModel.refreshFriendList()
        super.onDestroyView()
        Timber.e("bottomtest MainCardFragment onDestroyView")
    }

}
