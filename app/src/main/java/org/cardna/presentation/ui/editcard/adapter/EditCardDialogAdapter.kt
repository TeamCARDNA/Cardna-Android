package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardDialogBinding
import org.cardna.data.remote.model.card.CardData
import org.cardna.presentation.util.ItemTouchHelperListener
import timber.log.Timber

class EditCardDialogAdapter :
    ListAdapter<CardData, EditCardDialogAdapter.ViewHolder>(EditCardDialogComparator()) {

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
                    tvRepresentcardCount.apply {
                        visibility =
                            if (visibility == View.GONE) View.VISIBLE
                            else View.GONE
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