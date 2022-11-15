package org.cardna.presentation.ui.alarm.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.data.remote.model.alarm.ResponseGetAlarmData
import org.cardna.databinding.ItemAlarmFriendRequestBinding
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel
import timber.log.Timber

class FriendRequestAdapter(
    private val activity: Activity,
    private val alarmViewModel: AlarmViewModel,
    private val viewLifeCycleOwer: LifecycleOwner,
    private val clickListener: (ResponseGetAlarmData.Data.Request.Requester) -> Unit
) : androidx.recyclerview.widget.ListAdapter<ResponseGetAlarmData.Data.Request.Requester, FriendRequestAdapter.FriendRequestViewHolder>(diffUtil) {
    var loadStatus = true  //처음엔 접힌 상태로 시작

    inner class FriendRequestViewHolder(private val binding: ItemAlarmFriendRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ResponseGetAlarmData.Data.Request.Requester) {
            binding.apply {
                tvItemAlarmFriendRequestFriendName.text = data.name
                tvItemAlarmFriendRequestDate.text = data.date
                Glide.with(activity)
                    .load(data.profileImage)
                    .circleCrop()
                    .into(ivItemAlarmFriendRequestProfile)


                root.setOnClickListener {
                    clickListener(data)
                }

                mbItemAlarmFriendRequestAccept.setOnClickListener {
                    ctlItemAlarmFriendRequestBtn.visibility = View.INVISIBLE
                    ctlItemAlarmFriendRequestAccept.visibility = View.VISIBLE

                    tvItemAlarmFriendRequestAcceptFriendName.text = data.name
                    alarmViewModel.acceptOrDenyFriend(data.id, true)
                }

                mbItemAlarmFriendRequestRefuse.setOnClickListener {
                    alarmViewModel.acceptOrDenyFriend(data.id, false)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemAlarmFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun getItemCount() =
        if (loadStatus) {
            if (AlarmActivity.DEFAULT_COUNT > currentList.size) {
                currentList.size
            } else {
                AlarmActivity.DEFAULT_COUNT
            }
        } else {
            currentList.size
        }


    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ResponseGetAlarmData.Data.Request.Requester>() {
            override fun areContentsTheSame(oldItem: ResponseGetAlarmData.Data.Request.Requester, newItem: ResponseGetAlarmData.Data.Request.Requester) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ResponseGetAlarmData.Data.Request.Requester, newItem: ResponseGetAlarmData.Data.Request.Requester) =
                oldItem.id == newItem.id
        }
    }
}