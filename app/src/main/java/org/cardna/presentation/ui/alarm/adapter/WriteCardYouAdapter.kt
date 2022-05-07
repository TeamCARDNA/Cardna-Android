package org.cardna.presentation.ui.alarm.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.data.remote.model.alarm.ResponseAlarmData
import org.cardna.databinding.ItemAlarmWriteCardyouBinding


class WriteCardYouAdapter(
    private val activity: Activity,
    private val clickListener: (ResponseAlarmData.Data.Alarm) -> Unit
) : ListAdapter<ResponseAlarmData.Data.Alarm, WriteCardYouAdapter.WriteCardYouViewHolder>(diffUtil) {

    inner class WriteCardYouViewHolder(private val binding: ItemAlarmWriteCardyouBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ResponseAlarmData.Data.Alarm) {
            binding.apply {
                //TODO  서버연결 후 data 연결
                tvItemAlarmWriteCardyouFriendName.text = data.name
                tvItemAlarmWriteCardyouDate.text = data.date
                tvItemAlarmWriteCardyouSentence.text = data.content
                Glide.with(activity)
                    .load(data.profileImage)
                    .circleCrop()
                    .into(ivItemAlarmWriteCardyou)
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
        holder.onBind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ResponseAlarmData.Data.Alarm>() {
            override fun areContentsTheSame(oldItem: ResponseAlarmData.Data.Alarm, newItem: ResponseAlarmData.Data.Alarm) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ResponseAlarmData.Data.Alarm, newItem: ResponseAlarmData.Data.Alarm):Boolean {
                if (newItem.cardId == null) return oldItem.friendId == newItem.friendId
                else if (newItem.friendId == null)    return oldItem.cardId == newItem.cardId
                else return true
            }
        }
    }
}