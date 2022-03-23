package org.cardna.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cardna.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.databinding.ItemAlarmFriendRequestBinding
import org.cardna.presentation.ui.mypage.view.setting.AlarmActivity

class FriendRequestAdapter(
    private val friendRequestList: List<FriendRequestData>,
    private val clickListener: (FriendRequestData) -> Unit
) : androidx.recyclerview.widget.ListAdapter<FriendRequestData, FriendRequestAdapter.FriendRequestViewHolder>(diffUtil) {

    var defaultStatus = true

    inner class FriendRequestViewHolder(private val binding: ItemAlarmFriendRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: FriendRequestData) {
            binding.apply {

                //TODO  서버연결 후 data 연결
                tvItemAlarmFriendRequestFriendName.text = data.friendName
                tvItemAlarmFriendRequestDate.text = data.requestDate
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestAdapter.FriendRequestViewHolder {
        val binding = ItemAlarmFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun getItemCount() =
        if (defaultStatus) {
            AlarmActivity.DEFAULT_COUNT
        } else {
            friendRequestList.size
        }

    override fun onBindViewHolder(holder: FriendRequestAdapter.FriendRequestViewHolder, position: Int) {
        holder.onBind(friendRequestList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FriendRequestData>() {
            override fun areContentsTheSame(oldItem: FriendRequestData, newItem: FriendRequestData) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: FriendRequestData, newItem: FriendRequestData) =
                oldItem.friendName == newItem.friendName  //TODO 친구 id로 비교
        }
    }
}