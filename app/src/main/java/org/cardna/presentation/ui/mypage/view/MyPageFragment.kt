package org.cardna.presentation.ui.mypage.view

import android.content.Intent
import android.os.Bundle
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

    override fun onResume() {
        super.onResume()
        binding.etMypageNameSearchBackground.clearFocus()
        initData()
    }

    override fun initView() {
        initData()
        setStickyScroll()
        setMyPageFriendAdapter()
        setInputField()
        setObserve()
        initRootClickEvent(binding.ctlMypageTop)
        initRootClickEvent(binding.ctlMypageHeader)
    }

    private fun initData() {
        val query = myPageViewModel.searchNameQuery.value ?: ""
        if ((query.isNullOrEmpty() && myPageViewModel.updateSearchNameQuerySuccess.value == true) ||
            (query.isNullOrEmpty() && myPageViewModel.updateSearchNameQuerySuccess.value == false)
        ) {
            myPageViewModel.getUserMyPage()
            myPageViewModel.setUpdateSearchNameQueryState(false)
        } else if ((query.isNotEmpty() && myPageViewModel.updateSearchNameQuerySuccess.value == false)) {
            myPageViewModel.updateSearchNameQuery(query)
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

    private fun setMyPageFriendAdapter() {
        myPageFriendAdapter = MyPageFriendAdapter(requireActivity()) { item ->
            startActivity(
                Intent(requireContext(), MainCardActivity::class.java)
                    .putExtra("friendId", item.id)
                    .putExtra("name", item.name)
            )
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        with(binding) {
            rvMypage.layoutManager = gridLayoutManager
            rvMypage.adapter = myPageFriendAdapter
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
                        myPageViewModel.setUpdateSearchNameQueryState(true)
                        myPageViewModel.updateSearchNameQuerySuccess.observe(viewLifecycleOwner) {
                            if (it) initData()
                        }
                    }
                    return false
                }
            })
        }
    }

    private fun setObserve() {
        myPageViewModel.searchNameQuery.observe(viewLifecycleOwner) {
            myPageViewModel.searchNamePost()
        }

        if (binding.etMypageNameSearchBackground.query.isNullOrEmpty()) {
            myPageViewModel.myPage.observe(viewLifecycleOwner) { myPage ->
                myPageFriendAdapter.submitList(myPage.friendList)
                requireActivity().setSrcWithGlide(myPage.userImg, binding.ivMypageUserimg)
            }
        }
        myPageViewModel.searchFriendNameResult.observe(viewLifecycleOwner) { searchFriendNameResult ->
            myPageFriendAdapter.submitList(searchFriendNameResult)
        }
    }
}