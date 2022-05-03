package org.cardna.presentation.ui.alarm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.cardna.databinding.ItemAlarmFriendRequestBinding
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel

class FriendRequestAdapter(
    private val alarmViewModel: AlarmViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val clickListener: (FriendResponseData) -> Unit
) : androidx.recyclerview.widget.ListAdapter<FriendResponseData, FriendRequestAdapter.FriendRequestViewHolder>(diffUtil) {

    var defaultStatus = true

    inner class FriendRequestViewHolder(private val binding: ItemAlarmFriendRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: FriendResponseData) {
            binding.apply {

                //TODO  서버연결 후 data 연결
                tvItemAlarmFriendRequestFriendName.text = data.friendName
                tvItemAlarmFriendRequestDate.text = data.requestDate
                root.setOnClickListener {
                    clickListener(data)
                }
                //TODO  서버연결 후 수락 api
                mbItemAlarmFriendRequestAccept.setOnClickListener {
                    ctlItemAlarmFriendRequestBtn.visibility = View.INVISIBLE
                    ctlItemAlarmFriendRequestAccept.visibility = View.VISIBLE

                    tvItemAlarmFriendRequestAcceptFriendName.text = data.friendName
                    alarmViewModel.acceptOrDenyFriend(data.friendId, true)
                }

                //TODO  서버연결 후 거절 api
                mbItemAlarmFriendRequestRefuse.setOnClickListener {
                    alarmViewModel.acceptOrDenyFriend(data.friendId, false)
                }

                //친구 거절 시 아이템 삭제되어야하므로 친구 리스트 observe해서 submitlist하기


                if (defaultStatus) {
                    viewItemAlarmFriendRequestDiv.visibility = View.INVISIBLE
                } else {
                    viewItemAlarmFriendRequestDiv.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemAlarmFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun getItemCount() =
        if (defaultStatus) {
            AlarmActivity.DEFAULT_COUNT
        } else {
            currentList.size
        }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FriendResponseData>() {
            override fun areContentsTheSame(oldItem: FriendResponseData, newItem: FriendResponseData) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: FriendResponseData, newItem: FriendResponseData) =
                oldItem.friendName == newItem.friendName  //TODO 친구 id로 비교
        }
    }
}