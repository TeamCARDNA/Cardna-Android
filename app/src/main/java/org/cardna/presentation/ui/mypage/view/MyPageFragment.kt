package org.cardna.presentation.ui.mypage.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.FragmentMyPageBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.maincard.view.MainCardFragment
import org.cardna.presentation.ui.mypage.adapter.MyPageFriendAdapter
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
import org.cardna.presentation.ui.setting.view.SettingActivity
import org.cardna.presentation.util.*


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
        initData()
        initRootClickEvent(binding.ctlMypageContainer)
        initRootClickEvent(binding.ctlMypageHeader)
    }

    private fun initData() {
        myPageViewModel.getUserMyPage()
        setInitSearchResultStatus()
    }

    private fun setInitSearchResultStatus() {
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
                    MyPageViewModel.EXIST_QUERY -> myPageFriendAdapter.submitList(myPageViewModel.friendList.value?.reversed())//쿼리있는데 왔다가 온경우 ->업데이트 없어야함
                    MyPageViewModel.DEFAULT_STATE ->
                        myPageViewModel.friendList.observe(viewLifecycleOwner) { friendList ->
                            myPageFriendAdapter.submitList(friendList.reversed()) //가장 처음엔 운래 친구리스트
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
                            myPageFriendAdapter.submitList(it.reversed())
                        }
                    }
                    return false
                }
            })
        }
    }

    private fun setObserve() {
        //내 정보
        myPageViewModel.myPage.observe(viewLifecycleOwner) { myPage ->
            requireActivity().setSrcWithGlide(myPage.userImg, binding.ivMypageUserimg)
        }

        //클릭 가능 여부
        myPageViewModel.settingBtnIsValid.observe(viewLifecycleOwner) {
            binding.ivMypageSetting.isClickable = it
        }

        //친구 리프레시
        myPageViewModel.refreshFriendList.observe(viewLifecycleOwner) {
            setInitSearchResultStatus()
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

    private fun setHideKeyboard() {
        binding.scMypage.setOnClickListener {
            val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.etMypageNameSearchBackground.windowToken, 0)
        }
    }
}