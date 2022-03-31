package org.cardna.presentation.ui.mypage.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySearchFriendCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.setTextColor
import org.cardna.presentation.util.setTextSize

@AndroidEntryPoint
class SearchFriendCodeActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySearchFriendCodeBinding>(R.layout.activity_search_friend_code) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        setInputField()
        setObserve()
    }

    fun setInputField() {
        with(binding.etMypageCodeSearchBackground) {
            setTextSize(16f)
            setTextColor(this@SearchFriendCodeActivity, com.example.cardna.R.color.white_2, com.example.cardna.R.color.white_1)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(newText: String?): Boolean {
                    myPageViewModel.updateSearchCodeQuery(newText.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()){
                        //false로 바꾸기
                    }
                    return false
                }
            })
        }
    }

    private fun setObserve() {
        //검색 시키기
        myPageViewModel.searchCodeQuery.observe(this) {
            myPageViewModel.searchCodePost()
        }

        myPageViewModel.searchFriendCodeResult.observe(this) { searchFriendCodeResult ->
            if (searchFriendCodeResult != null){
                //이미지 세팅
            }
        }
    }
}