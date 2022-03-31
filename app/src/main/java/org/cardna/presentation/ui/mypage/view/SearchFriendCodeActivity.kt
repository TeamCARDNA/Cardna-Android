package org.cardna.presentation.ui.mypage.view

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySearchFriendCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class SearchFriendCodeActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySearchFriendCodeBinding>(R.layout.activity_search_friend_code) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}