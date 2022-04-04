package org.cardna.presentation.ui.mypage.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySearchFriendCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
import org.cardna.presentation.util.*

@AndroidEntryPoint
class SearchFriendCodeActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySearchFriendCodeBinding>(R.layout.activity_search_friend_code) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.myPageViewModel = myPageViewModel
        binding.searchFriendCodeActivity = this
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        setInputField()
        setObserve()
        initRootClickEvent(binding.ctlMypageCodeSearchContainer)
    }

    fun setInputField() {
        with(binding.etMypageCodeSearchBackground) {
            setTextSize(16f)
            setTextColor(this@SearchFriendCodeActivity, com.example.cardna.R.color.white_2, com.example.cardna.R.color.white_1)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(newText: String?): Boolean {
                    myPageViewModel.updateSearchCodeQuery(newText.toString())
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        binding.ctlMypageCodeSearch.visibility = View.INVISIBLE
                    }
                    return false
                }
            })
        }
    }

    private fun setObserve() {
        myPageViewModel.searchCodeQuery.observe(this) {
            myPageViewModel.searchCodePost()
        }

        myPageViewModel.searchFriendCodeResult.observe(this) {
            if (it.userImg.isNotEmpty())  //TODO 현재 서버에서 유저 이미지 temp로 와서 안뜸
                this.setSrcWithGlide(
                    it.userImg, binding.ivMypageCodeSearch
                )
        }
    }

    @SuppressLint("ResourceType")
    fun showCancelFriendRequestDialog() {
        val dialog = this.showCustomDialog(R.layout.dialog_cancle_friend_request)
        val confirmBtn = dialog.findViewById<Button>(R.id.tv_cancle_friend_request_dialog_confirm)
        val cancelBtn = dialog.findViewById<Button>(R.id.tv_cancle_friend_request_dialog_cancel)

        //TODO 로그아웃 확인 누르면 서버 통신
        confirmBtn.setOnClickListener {
            myPageViewModel.cancelFriendRequest()
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("ResourceType")
    fun showBreakUpFriendDialog() {
        val dialog = this.showCustomDialog(R.layout.dialog_breakup_friend)
        val confirmBtn = dialog.findViewById<Button>(R.id.tv_dialog_break_up_friend_title_confirm)
        val cancelBtn = dialog.findViewById<Button>(R.id.tv_dialog_break_up_friend_title_cancle)

        //TODO 로그아웃 확인 누르면 서버 통신
        confirmBtn.setOnClickListener {
            myPageViewModel.breakUpFriend()
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    companion object {
        const val RELATION_ONE = 1 //모르는 인간
        const val RELATION_TWO = 2 //친구
        const val RELATION_THREE = 3 //요청중
    }
}