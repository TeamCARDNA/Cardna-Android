package org.cardna.presentation.ui.alarm.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.data.remote.model.alarm.ResponseAlarmData
import org.cardna.databinding.ItemAlarmFriendRequestBinding
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.alarm.viewmodel.AlarmViewModel

class FriendRequestAdapter(
    private val activity: Activity,
    private val alarmViewModel: AlarmViewModel,
    private val clickListener: (ResponseAlarmData.Data.Request.Requester) -> Unit
) : androidx.recyclerview.widget.ListAdapter<ResponseAlarmData.Data.Request.Requester, FriendRequestAdapter.FriendRequestViewHolder>(diffUtil) {

    var defaultStatus = true

    inner class FriendRequestViewHolder(private val binding: ItemAlarmFriendRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ResponseAlarmData.Data.Request.Requester) {
            binding.apply {

                //TODO  서버연결 후 data 연결
                tvItemAlarmFriendRequestFriendName.text = data.name
                tvItemAlarmFriendRequestDate.text = data.date
                Glide.with(activity)
                    .load(data.profileImage)
                    .circleCrop()
                    .into(ivItemAlarmFriendRequestProfile)

                //친구 메인뷰로 이동
                root.setOnClickListener {
                    clickListener(data)
                }
                //TODO  서버연결 후 수락 api
                mbItemAlarmFriendRequestAccept.setOnClickListener {
                    ctlItemAlarmFriendRequestBtn.visibility = View.INVISIBLE
                    ctlItemAlarmFriendRequestAccept.visibility = View.VISIBLE

                    tvItemAlarmFriendRequestAcceptFriendName.text = data.name
                    alarmViewModel.acceptOrDenyFriend(data.id, true)
                }

                //TODO  서버연결 후 거절 api
                mbItemAlarmFriendRequestRefuse.setOnClickListener {
                    alarmViewModel.acceptOrDenyFriend(data.id, false)
                }

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
        holder.onBind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ResponseAlarmData.Data.Request.Requester>() {
            override fun areContentsTheSame(oldItem: ResponseAlarmData.Data.Request.Requester, newItem: ResponseAlarmData.Data.Request.Requester) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ResponseAlarmData.Data.Request.Requester, newItem: ResponseAlarmData.Data.Request.Requester) =
                oldItem.id == newItem.id
        }
    }
}