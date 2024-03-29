package org.cardna.presentation.ui.alarm.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.data.remote.model.card.RequestEditCardData
import org.cardna.databinding.ItemAlarmWriteCardyouBinding
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel
import javax.inject.Inject


class WriteCardYouAdapter(
    private val activity: Activity,
    private val clickListener: (ResponseGetAlarmData.Data.Alarm) -> Unit
) : ListAdapter<ResponseGetAlarmData.Data.Alarm, WriteCardYouAdapter.WriteCardYouViewHolder>(diffUtil) {

    inner class WriteCardYouViewHolder(private val binding: ItemAlarmWriteCardyouBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ResponseGetAlarmData.Data.Alarm) {
            binding.apply {
                //TODO  서버연결 후 data 연결
                tvItemAlarmWriteCardyouDate.text = data.date
                tvItemAlarmWriteCardyouSentence.text = data.content
                tvItemAlarmWriteCardyouFriendName.text = data.name


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
        val diffUtil = object : DiffUtil.ItemCallback<ResponseGetAlarmData.Data.Alarm>() {
            override fun areContentsTheSame(oldItem: ResponseGetAlarmData.Data.Alarm, newItem: ResponseGetAlarmData.Data.Alarm) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ResponseGetAlarmData.Data.Alarm, newItem: ResponseGetAlarmData.Data.Alarm): Boolean {
                if (newItem.cardId == null) return oldItem.friendId == newItem.friendId
                else if (newItem.friendId == null) return oldItem.cardId == newItem.cardId
                else return true
            }
        }
    }
}