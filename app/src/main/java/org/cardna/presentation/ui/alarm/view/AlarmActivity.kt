package org.cardna.presentation.ui.alarm.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.alarm.adapter.FriendResponseData
import org.cardna.presentation.ui.alarm.adapter.WriteCardYouAdapter
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.StatusBarUtil

@AndroidEntryPoint
class AlarmActivity : BaseViewUtil.BaseAppCompatActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    private lateinit var friendRequestAdapter: FriendRequestAdapter
    private lateinit var writeCardYouAdapter: WriteCardYouAdapter
    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.alarmViewModel = alarmViewModel
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        initData()
        setFriendRequestAdapter()
        setWriteCardYouAdapter()
        setObserve()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        alarmViewModel.getFriendRequest()
        alarmViewModel.getWriteCardYou()
    }

    private fun setObserve() {
        //TODO api연결 후 정의
/*        alarmViewModel.friendRequest.observe(this) { friendRequest ->
            friendRequestAdapter.submitList(friendRequest)
        }
        alarmViewModel.writeCardYou.observe(this) { writeCardYou ->
            writeCardYouAdapter.submitList(friendRequest)
        }

        alarmViewModel.deletePostSuccess.observe(this) { deleteSuccess ->
            if (deleteSuccess)
                alarmViewModel.getFriendRequest()
        }

        alarmViewModel.deletePostSuccess.observe(this) { deleteSuccess ->
            if (deleteSuccess)
                alarmViewModel.getFriendRequest()
        }*/
    }

    private fun setFriendRequestAdapter() {
        //TODO 서버연결 후 삭제
        val dataList = mutableListOf(
            FriendResponseData("김다빈", "2022/24/42", 1),
            FriendResponseData("박민우", "2022/24/42", 2),
            FriendResponseData("이종찬", "2022/24/42", 3),
            FriendResponseData("김다빈", "2022/24/42", 4),
        )

        //TODO 친구 대표카드 뷰로 이동
        friendRequestAdapter = FriendRequestAdapter(alarmViewModel, this) { item ->
            //    startActivity(Intent(this, DetailInfoActivity::class.java))
        }
        with(binding.rcvAlarmFriendRequest) {
            adapter = friendRequestAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            setUnfoldListener(friendRequestAdapter)
            friendRequestAdapter.submitList(dataList)
        }
    }

    private fun setWriteCardYouAdapter() {
        val dataList = mutableListOf(
            FriendResponseData("김다빈", "2022/24/42", 1),
            FriendResponseData("박민우", "2022/24/42", 2),
            FriendResponseData("이종찬", "2022/24/42", 3),
        )


        //TODO 카드상세 페이지로 이동->카드 아이디 보내서 이동 friendId cardId로 변경
        writeCardYouAdapter = WriteCardYouAdapter { item ->
            val intent = Intent(this, DetailCardActivity::class.java).let {
                it.putExtra(BaseViewUtil.CARD_ID, item.friendId)
            }
            startActivity(intent)
        }
        with(binding.rcvAlarmWriteCardyou) {
            adapter = writeCardYouAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            writeCardYouAdapter.submitList(dataList)  //TODO 서버연결 후 삭제
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