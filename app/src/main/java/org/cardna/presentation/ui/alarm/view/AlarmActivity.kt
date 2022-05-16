package org.cardna.presentation.ui.alarm.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivityAlarmBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.alarm.adapter.WriteCardYouAdapter
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.maincard.view.MainCardActivity
import org.cardna.presentation.util.DividerItemDecoration
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.convertDPtoPX

@AndroidEntryPoint
class AlarmActivity : BaseViewUtil.BaseAppCompatActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    private lateinit var friendRequestAdapter: FriendRequestAdapter
    private lateinit var writeCardYouAdapter: WriteCardYouAdapter
    private lateinit var dividerItemDecoration: DividerItemDecoration
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
        alarmViewModel.geAllAlarm()
    }

    private fun setObserve() {

        alarmViewModel.friendRequest.observe(this) { friendRequest ->
            friendRequestAdapter.submitList(friendRequest)
        }

        alarmViewModel.writeCardYou.observe(this) { writeCardYou ->
            writeCardYouAdapter.submitList(writeCardYou)
        }

        alarmViewModel.isRequestDeny.observe(this) { isRequestDeny ->
            if (isRequestDeny) initData()
        }
    }

    private fun setFriendRequestAdapter() {
        friendRequestAdapter = FriendRequestAdapter(this, alarmViewModel, this) { item ->
            startActivity(Intent(this, MainCardActivity::class.java).apply {
                putExtra("friendId", item.id)
                putExtra("name", item.name)
            })
        }
        with(binding.rcvAlarmFriendRequest) {
            adapter = friendRequestAdapter
            dividerItemDecoration = DividerItemDecoration(this@AlarmActivity, R.drawable.bgbgbgbgbg, 0,0)
            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            setUnfoldListener(friendRequestAdapter)
        }
    }

    private fun setWriteCardYouAdapter() {
        writeCardYouAdapter = WriteCardYouAdapter(this) { item ->
            val intent = Intent(this, DetailCardActivity::class.java)
                .putExtra(BaseViewUtil.CARD_ID, item.cardId)
            startActivity(intent)
        }
        with(binding.rcvAlarmWriteCardyou) {
            adapter = writeCardYouAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
        }
    }

    private fun setUnfoldListener(adapter: FriendRequestAdapter) {

        binding.tvAlarmFriendViewAll.setOnClickListener {
            if (adapter.loadStatus) {
                binding.tvAlarmFriendViewAll.text = COLLAPSE_LIST
                adapter.loadStatus = false
            } else {
                binding.tvAlarmFriendViewAll.text = VIEW_ALL
                adapter.loadStatus = true
            }
            friendRequestAdapter.notifyDataSetChanged()  //리스트 크기 매번 변경해야함으로 사용
        }
    }

    private fun submitFriendRequestList() {
        alarmViewModel.friendRequest.observe(this) {
            friendRequestAdapter.submitList(it)
            Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "$it")
        }
    }

    companion object {
        const val VIEW_ALL = "모두 보기"
        const val COLLAPSE_LIST = "목록 접기"
        const val DEFAULT_COUNT = 1
    }
}