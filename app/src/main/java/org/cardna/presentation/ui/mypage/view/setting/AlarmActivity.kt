package org.cardna.presentation.ui.mypage.view.setting

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.mypage.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.mypage.adapter.FriendRequestData
import org.cardna.presentation.util.HorizontalItemDecorator
import org.cardna.presentation.util.convertDPtoPX

@AndroidEntryPoint
class AlarmActivity : BaseViewUtil.BaseAppCompatActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    private lateinit var friendRequestAdapter: FriendRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setFriendRequestAdapter()
     //   setWriteCardYouAdapter()
    }

    private fun setFriendRequestAdapter() {
        val dataList = mutableListOf(
            FriendRequestData("김다빈", "2022/24/42"),
            FriendRequestData("박민우", "2022/24/42"),
            FriendRequestData("이종찬", "2022/24/42"),

            )
        friendRequestAdapter = FriendRequestAdapter(dataList) { item ->
            //  startActivity(Intent(this, DetailInfoActivity::class.java))  //TODO 친구 대표카드 뷰로 이동
        }
        with(binding.rcvAlarmFriendRequest) {
            adapter = friendRequestAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            addItemDecoration(HorizontalItemDecorator(this@AlarmActivity, R.drawable.divider_white_5_1dp, convertDPtoPX(30)))
            setUnfoldListener(friendRequestAdapter)
        }
    }

    private fun setUnfoldListener(adapter: FriendRequestAdapter) {
        with(binding) {
            tvAlarmFriendViewAll.setOnClickListener {
                if (adapter.defaultStatus) {
                    tvAlarmFriendViewAll.text = COLLAPSE_LIST
                    adapter.defaultStatus = false
                } else {
                    tvAlarmFriendViewAll.text = VIEW_ALL
                    adapter.defaultStatus = true
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val VIEW_ALL = "모두 보기"
        const val COLLAPSE_LIST = "목록 접기"
        const val DEFAULT_COUNT = 1
    }
}