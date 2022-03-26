package org.cardna.presentation.ui.alarm.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardna.R
import com.example.cardna.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.alarm.adapter.FriendRequestData
import org.cardna.presentation.ui.alarm.adapter.WriteCardYouAdapter
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel

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
        initData()
        setFriendRequestAdapter()
        setWriteCardYouAdapter()
        setFriendRequestEmptyObserve()
        setWriteCardYouEmptyObserve()
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

    //TODO 친구수락 버튼 클릭 api
    private fun friendRequestAccept(FriendRequestData: FriendRequestData) {
        //    alarmViewModel.updateComment(comment)
    }

    //TODO 친구거절 버튼 클릭 api
    private fun friendRequestRefuse(FriendRequestData: FriendRequestData) {
        //   alarmViewModel.updateComment(comment)
    }

    private fun setFriendRequestEmptyObserve() {
        alarmViewModel.isFriendRequestEmpty.observe(this) { isFriendRequestEmpty ->
            if (isFriendRequestEmpty) {
                binding.ctlAlarmBg.visibility = View.GONE
            } else {
                binding.ctlAlarmBg.visibility = View.VISIBLE
            }
        }
    }

    private fun setWriteCardYouEmptyObserve() {
        alarmViewModel.isWriteCardYouEmpty.observe(this) { isWriteCardYouEmpty ->
            if (isWriteCardYouEmpty) {
                binding.rcvAlarmWriteCardyou.visibility = View.GONE
            } else {
                binding.rcvAlarmWriteCardyou.visibility = View.VISIBLE
            }
        }
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
            FriendRequestData("김다빈", "2022/24/42"),
            FriendRequestData("박민우", "2022/24/42"),
            FriendRequestData("이종찬", "2022/24/42"),
            FriendRequestData("김다빈", "2022/24/42"),
        )

        //TODO 친구 대표카드 뷰로 이동
        friendRequestAdapter = FriendRequestAdapter(::friendRequestAccept, ::friendRequestRefuse) { item ->
            //  startActivity(Intent(this, DetailInfoActivity::class.java))
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