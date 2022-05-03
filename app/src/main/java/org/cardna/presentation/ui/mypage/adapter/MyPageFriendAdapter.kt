package org.cardna.presentation.ui.mypage.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.databinding.ItemMypageFriendBinding


class MyPageFriendAdapter(
    private val activity: Activity,
    private val clickListener: ((ResponseMyPageData.Data.FriendList) -> Unit),
) : ListAdapter<ResponseMyPageData.Data.FriendList, MyPageFriendAdapter.MyPageFriendViewHolder>(FriendComparator()) {

    inner class MyPageFriendViewHolder(
        private val binding: ItemMypageFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseMyPageData.Data.FriendList) {
            with(binding) {
                tvFriendName.text = data.name
                tvFriendSentence.text = data.sentence
                Glide.with(activity)
                    .load(data.userImg)
                    .circleCrop()
                    .into(ivFriendUserimg)

                clRvItem.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageFriendViewHolder {
        val binding = ItemMypageFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageFriendViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    private class FriendComparator : DiffUtil.ItemCallback<ResponseMyPageData.Data.FriendList>() {
        override fun areItemsTheSame(
            oldItem: ResponseMyPageData.Data.FriendList,
            newItem: ResponseMyPageData.Data.FriendList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseMyPageData.Data.FriendList,
            newItem: ResponseMyPageData.Data.FriendList
        ): Boolean {
            return oldItem == newItem
        }
    }
}