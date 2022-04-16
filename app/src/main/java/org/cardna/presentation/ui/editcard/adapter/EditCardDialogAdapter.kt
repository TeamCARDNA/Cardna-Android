package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardDialogBinding
import org.cardna.data.remote.model.card.CardData

class EditCardDialogAdapter :
    ListAdapter<CardData, EditCardDialogAdapter.ViewHolder>(EditCardDialogComparator()) {
    private val selectedItem = mutableListOf<Int>()

    inner class ViewHolder(private val binding: ItemEditCardDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CardData) {
            with(binding) {
                Glide
                    .with(itemView.context)
                    .load(data.cardImg)
                    .into(ivCardpackRecyclerview)

                tvEditcarddialogTitle.text = data.title
                clRvItem.setBackgroundResource(setBackground(data.isMe))

                itemView.setOnClickListener {
                    binding.tvRepresentcardCount.apply {
                        visibility =
                            if (visibility == View.GONE) {
                                selectedItem.add(data.id)
                                text = selectedItem.size.toString()
                                View.VISIBLE
                            } else {
                                selectedItem.removeAt(selectedItem.indexOf(data.id))
                                View.GONE
                            }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: EditCardDialogAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditCardDialogAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEditCardDialogBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class EditCardDialogComparator : DiffUtil.ItemCallback<CardData>() {
        override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem == newItem
        }
    }

    private fun setBackground(isMe: Boolean): Int {
        return if (isMe) {
            R.drawable.bg_main_green_radius_8
        } else {
            R.drawable.bg_main_purple_radius_8
        }
    }

}