package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardBinding
import org.cardna.data.remote.model.card.ResponseMainCardData

class EditCardAdapter :
    ListAdapter<ResponseMainCardData.Data.MainCard, EditCardAdapter.ViewHolder>(EditCardComparator()) {
    inner class ViewHolder(private val binding: ItemEditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseMainCardData.Data.MainCard) {
            with(binding) {
                Glide
                    .with(itemView.context)
                    .load(data.cardImg)
                    .into(ivRepresentcardeditlistImage)
                tvRepresentcardlistUserTag.text = data.title

                if (data.isMe) {
                    clRvItem.setBackgroundResource(R.drawable.bg_main_green_radius_8)
                } else {
                    clRvItem.setBackgroundResource(R.drawable.bg_main_purple_radius_8)
                }

                ivRepresentcardeditlistDelete.setOnClickListener {
                    val newList = currentList.toMutableList()
                    newList.removeAt(adapterPosition)
                    submitList(newList)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEditCardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.onBind(data)
    }

    class EditCardComparator : DiffUtil.ItemCallback<ResponseMainCardData.Data.MainCard>() {
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
            return oldItem == newItem
        }
    }
}