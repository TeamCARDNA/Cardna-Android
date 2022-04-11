package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardBinding
import org.cardna.data.remote.model.card.MainCard
import org.cardna.presentation.util.ItemTouchHelperListener

class EditCardAdapter :
    ListAdapter<MainCard, EditCardAdapter.ViewHolder>(EditCardComparator()),
    ItemTouchHelperListener {
    inner class ViewHolder(private val binding: ItemEditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: MainCard) {
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
                    setNewList(adapterPosition)
                }
            }
        }
    }

    private fun setNewList(adapterPosition: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(adapterPosition)
        submitList(newList)
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

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        val item = currentList[from_position]
        currentList.removeAt(from_position)
        currentList.add(to_position, item)
        notifyItemMoved(from_position, to_position)
        return true
    }

    override fun onItemSwipe(position: Int) {

    }

    class EditCardComparator : DiffUtil.ItemCallback<MainCard>() {
        override fun areItemsTheSame(
            oldItem: MainCard,
            newItem: MainCard
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MainCard,
            newItem: MainCard
        ): Boolean {
            return oldItem == newItem
        }
    }

}