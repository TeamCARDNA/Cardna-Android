package org.cardna.presentation.ui.mypage.view.setting

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.mypage.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.mypage.adapter.FriendRequestData
import org.cardna.presentation.ui.mypage.adapter.WriteCardYouAdapter

@AndroidEntryPoint
class AlarmActivity : BaseViewUtil.BaseAppCompatActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    private lateinit var friendRequestAdapter: FriendRequestAdapter
    private lateinit var writeCardYouAdapter: WriteCardYouAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setFriendRequestAdapter()
        setWriteCardYouAdapterr()
    }

    private fun setFriendRequestAdapter() {
        //TODO 서버연결 후 삭제
        val dataList = mutableListOf(
            FriendRequestData("김다빈", "2022/24/42"),
            FriendRequestData("박민우", "2022/24/42"),
            FriendRequestData("이종찬", "2022/24/42"),
            FriendRequestData("김다빈", "2022/24/42"),
            )

        //TODO 친구 대표카드 뷰로 이동
        friendRequestAdapter = FriendRequestAdapter { item ->
            //  startActivity(Intent(this, DetailInfoActivity::class.java))
        }
        with(binding.rcvAlarmFriendRequest) {
            adapter = friendRequestAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            setUnfoldListener(friendRequestAdapter)
            friendRequestAdapter.submitList(dataList)
        }
    }

    private fun setWriteCardYouAdapterr() {
        val dataList = mutableListOf(
            FriendRequestData("김다빈", "2022/24/42"),
            FriendRequestData("박민우", "2022/24/42"),
            FriendRequestData("이종찬", "2022/24/42"),
        )

        //TODO 카드상세 페이지로 이동
        writeCardYouAdapter = WriteCardYouAdapter { item ->
            //  startActivity(Intent(this, DetailInfoActivity::class.java))
        }
        with(binding.rcvAlarmWriteCardyou) {
            adapter = writeCardYouAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            writeCardYouAdapter.submitList(dataList)
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
                adapter.notifyDataSetChanged() //TODO viewmodel연결
            }
        }
    }

    companion object {
        const val VIEW_ALL = "모두 보기"
        const val COLLAPSE_LIST = "목록 접기"
        const val DEFAULT_COUNT = 1
    }
}