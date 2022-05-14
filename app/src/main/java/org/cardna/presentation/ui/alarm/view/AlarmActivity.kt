package org.cardna.presentation.ui.alarm.view

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivityAlarmBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.adapter.FriendRequestAdapter
import org.cardna.presentation.ui.alarm.adapter.FriendResponseData
import org.cardna.presentation.ui.alarm.adapter.WriteCardYouAdapter
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.ui.maincard.view.MainCardActivity
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
        alarmViewModel.geAllAlarm()
    }

    private fun setObserve() {
        /*   alarmViewModel.isFriendRequestEmpty.observe(this) { isFriendRequestEmpty ->
               if (!isFriendRequestEmpty) setFriendRequestAdapter()
           }*/
        alarmViewModel.friendRequest.observe(this) { friendRequest ->
            friendRequestAdapter.submitList(friendRequest)
        }

        /* alarmViewModel.isWriteCardYouEmpty.observe(this) { isWriteCardYouEmpty ->
             if (!isWriteCardYouEmpty) setWriteCardYouAdapter()
         }*/
        alarmViewModel.writeCardYou.observe(this) { writeCardYou ->
            writeCardYouAdapter.submitList(writeCardYou)
        }

        alarmViewModel.isRequestDeny.observe(this) { isRequestDeny ->
            if (isRequestDeny) initData()
        }
    }

    private fun setFriendRequestAdapter() {
        friendRequestAdapter = FriendRequestAdapter(this, alarmViewModel) { item ->
            startActivity(Intent(this, MainCardActivity::class.java).apply {
                putExtra("friendId", item.id)
            })
        }
        with(binding.rcvAlarmFriendRequest) {
            adapter = friendRequestAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
            setUnfoldListener(friendRequestAdapter)
        }
    }

    private fun setWriteCardYouAdapter() {
        writeCardYouAdapter = WriteCardYouAdapter(this) { item ->
            val intent = Intent(this, DetailCardActivity::class.java).let {
                it.putExtra(BaseViewUtil.CARD_ID, item.friendId)
            }
            startActivity(intent)
        }
        with(binding.rcvAlarmWriteCardyou) {
            adapter = writeCardYouAdapter
            layoutManager = LinearLayoutManager(this@AlarmActivity)
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
                adapter.submitList(adapter.currentList)
            }
        }
    }

    companion object {
        const val VIEW_ALL = "모두 보기"
        const val COLLAPSE_LIST = "목록 접기"
        const val DEFAULT_COUNT = 1
    }
}