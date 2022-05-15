package org.cardna.presentation.ui.mypage.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import org.cardna.R
import org.cardna.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.maincard.view.MainCardActivity
import org.cardna.presentation.ui.maincard.view.MainCardFragment
import org.cardna.presentation.ui.mypage.adapter.MyPageFriendAdapter
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
import org.cardna.presentation.ui.setting.view.SettingActivity
import org.cardna.presentation.util.*
import timber.log.Timber


@AndroidEntryPoint
class MyPageFragment : BaseViewUtil.BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private lateinit var myPageFriendAdapter: MyPageFriendAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myPageViewModel = myPageViewModel
        binding.myPageFragment = this
        initView()
    }

    override fun initView() {
        binding.etMypageNameSearchBackground.clearFocus()
        setSearchFriendNameResultObserve()
        setStickyScroll()
        setMyPageFriendAdapter()
        setInputField()
        setObserve()
        copyMyCodeClickListener()
        setSettingBtnValidObserve()
        initData()
        initRootClickEvent(binding.ctlMypageTop)
        initRootClickEvent(binding.ctlMypageHeader)
    }

    private fun initData() {
        myPageViewModel.getUserMyPage()
        setInitSearchResultStatus()
    }

    private fun setInitSearchResultStatus() {
        Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "${myPageViewModel.searchNameQuery.value}" + "${myPageViewModel.isNonExistFriend.value}")
        if (myPageViewModel.searchNameQuery.value?.isNotEmpty() == true && myPageViewModel.isNonExistFriend.value == false) {
            myPageFriendAdapter.submitList(myPageViewModel.searchFriendNameResult.value)
        } else if (myPageViewModel.searchNameQuery.value?.isNotEmpty() == true && myPageViewModel.isNonExistFriend.value == true) {
            return
        } else {
            myPageViewModel.getUserMyPage()
            myPageViewModel.setQueryState(MyPageViewModel.DEFAULT_STATE)
        }
    }

    private fun setStickyScroll() {
        binding.scMypage.run {
            header = binding.ctlMypageHeader
        }
    }

    fun setSettingClickListener() {
        startActivity(Intent(requireContext(), SettingActivity::class.java))
    }

    fun setSearchCodeClickListener() {
        startActivity(Intent(requireContext(), SearchFriendCodeActivity::class.java))
    }


    //TODO 쿼리 상태 쨰려서 뷰 업데이트
    private fun setSearchFriendNameResultObserve() {
        myPageViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    MyPageViewModel.SEARCH_QUERY -> myPageFriendAdapter.submitList(myPageViewModel.searchFriendNameResult.value)//진짜 검색해서 결과뜬 경우
                    MyPageViewModel.EXIST_QUERY -> myPageFriendAdapter.submitList(myPageViewModel.friendList.value)//쿼리있는데 왔다가 온경우 ->업데이트 없어야함
                    MyPageViewModel.DEFAULT_STATE ->
                        myPageViewModel.friendList.observe(viewLifecycleOwner) { friendList ->
                            myPageFriendAdapter.submitList(friendList) //가장 처음엔 운래 친구리스트
                        }
                }
            }
        }
    }


    fun setInputField() {
        with(binding.etMypageNameSearchBackground) {
            setTextSize(16f)
            setTextColor(requireContext(), R.color.white_2, R.color.white_1)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(newText: String?): Boolean {
                    if (newText?.isNotEmpty() == true) {
                        myPageViewModel.updateSearchNameQuery(newText.toString())
                        clearFocus()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        myPageViewModel.updateSearchNameQuery("")
                        myPageViewModel.isNonExistFriendName(false)
                        myPageViewModel.friendList.observe(viewLifecycleOwner) {
                            myPageFriendAdapter.submitList(it)
                        }
                    }
                    return false
                }
            })
        }
    }

    private fun setObserve() {

        myPageViewModel.myPage.observe(viewLifecycleOwner) { myPage ->
            //todo 맨처음에는 마이페이지 친구 리스트 던져야함
            requireActivity().setSrcWithGlide(myPage.userImg, binding.ivMypageUserimg)
        }
    }

    private fun setMyPageFriendAdapter() {
        myPageFriendAdapter = MyPageFriendAdapter(requireActivity(), myPageViewModel) { item ->
            val bundle = Bundle().apply {
                putInt(BaseViewUtil.ID, item.id)  //친구 아이디
                putString("name", item.name)
                putString("sentence", item.sentence)
            }
            val mainCardFragment = MainCardFragment()
            mainCardFragment.arguments = bundle

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.fcv_main, mainCardFragment)
            transaction.commit()
        }
        binding.rvMypage.addItemDecoration(MyPageItemVerticalDecoration())
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        with(binding) {
            rvMypage.layoutManager = gridLayoutManager
            rvMypage.adapter = myPageFriendAdapter
        }

    }

    private fun copyMyCodeClickListener() {
        binding.ivMypageCode.setOnClickListener {
            createClipData(binding.tvMypageCode.text.toString())
        }
    }

    private fun createClipData(message: String) {
        val clipBoardManger: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("TAG", message)
        clipBoardManger.setPrimaryClip(clipData)
        requireContext().shortToast("코드가 복사되었습니다")
    }

    private fun setSettingBtnValidObserve() {
        myPageViewModel.settingBtnIsValid.observe(viewLifecycleOwner) {
            binding.ivMypageSetting.isClickable = it
            if (it) setInitSearchResultStatus()
        }
    }
}