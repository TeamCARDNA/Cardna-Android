package org.cardna.presentation.ui.alarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.cardna.databinding.ItemAlarmWriteCardyouBinding

class WriteCardYouAdapter(
    private val clickListener: (FriendResponseData) -> Unit
) : androidx.recyclerview.widget.ListAdapter<FriendResponseData, WriteCardYouAdapter.WriteCardYouViewHolder>(diffUtil) {

    inner class WriteCardYouViewHolder(private val binding: ItemAlarmWriteCardyouBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: FriendResponseData) {
            binding.apply {

                //TODO  서버연결 후 data 연결
                tvItemAlarmWriteCardyouFriendName.text = data.friendName
                tvItemAlarmWriteCardyouDate.text = data.requestDate
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteCardYouViewHolder {
        val binding = ItemAlarmWriteCardyouBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WriteCardYouViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WriteCardYouViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FriendResponseData>() {
            override fun areContentsTheSame(oldItem: FriendResponseData, newItem: FriendResponseData) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: FriendResponseData, newItem: FriendResponseData) =
                oldItem.friendName == newItem.friendName  //TODO 친구 id로 비교
        }
    }
}