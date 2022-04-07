package org.cardna.presentation.ui.maincard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemMainCardViewBinding
import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.data.remote.model.mypage.ResponseMyPageData
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import timber.log.Timber

class MainCardAdapter() :
    ListAdapter<ResponseMainCardData.Data.MainCard, MainCardAdapter.ViewHolder>(MainCardComparator()) {

    inner class ViewHolder(private val binding: ItemMainCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: ResponseMainCardData.Data.MainCard
        ) {
            with(binding) {
                Glide
                    .with(itemView.context)
                    .load(data.cardImg)
                    .into(ivMainCardImage)
                tvMainCardTitle.text = data.title
                clMaincardContainer.setBackgroundResource(
                    if (data.isMe) {
                        R.drawable.bg_green_null_black_radius_2

                    } else {
                        R.drawable.bg_right_null_black_radius_2
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMainCardViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.onBind(data)
    }

    private class MainCardComparator : DiffUtil.ItemCallback<ResponseMainCardData.Data.MainCard>() {
        override fun areItemsTheSame(
            oldItem: ResponseMainCardData.Data.MainCard,
            newItem: ResponseMainCardData.Data.MainCard
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseMainCardData.Data.MainCard,
            newItem: ResponseMainCardData.Data.MainCard
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

}