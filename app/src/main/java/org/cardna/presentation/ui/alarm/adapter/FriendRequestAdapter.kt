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

class FriendRequestAdapter(
    private val activity: Activity,
    private val alarmViewModel: AlarmViewModel,
    private val viewLifeCycleOwer: LifecycleOwner,
    private val clickListener: (ResponseGetAlarmData.Data.Request.Requester) -> Unit
) : androidx.recyclerview.widget.ListAdapter<ResponseGetAlarmData.Data.Request.Requester, FriendRequestAdapter.FriendRequestViewHolder>(diffUtil) {


    inner class FriendRequestViewHolder(private val binding: ItemAlarmFriendRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ResponseGetAlarmData.Data.Request.Requester) {
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
                alarmViewModel.foldStatus.observe(viewLifeCycleOwer) { foldStatus ->
                    if (foldStatus) {
                        Log.d("ㅡㅡㅡㅡㅡㅡㅡ어댑터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", foldStatus.toString())
                        viewItemAlarmFriendRequestDiv.visibility = View.INVISIBLE
                    } else {
                        Log.d("ㅡㅡㅡㅡㅡㅡㅡ어댑터ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", foldStatus.toString())
                        viewItemAlarmFriendRequestDiv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemAlarmFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun getItemCount() =
        if (alarmViewModel.foldStatus.value == true) {
            AlarmActivity.DEFAULT_COUNT
            Log.d("ㅡㅡㅡㅡㅡㅡㅡcDEFAULT_COUNTㅡㅡㅡㅡㅡㅡㅡ", currentList.size.toString())
        } else {
            currentList.size-1
            Log.d("ㅡㅡㅡㅡㅡㅡㅡcurrentListㅡㅡㅡㅡㅡㅡㅡ", currentList.size.toString())
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