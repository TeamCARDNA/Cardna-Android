package org.cardna.presentation.ui.maincard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.databinding.ItemMainCardViewBinding
import org.cardna.domain.model.RepresentCardListData

class MainCardAdapter(private val onItemClick: ((RepresentCardListData.Data) -> Unit?)) :
    ListAdapter<RepresentCardListData.Data, MainCardAdapter.ViewHolder>(MainCardComparator()) {

    inner class ViewHolder(private val binding: ItemMainCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: RepresentCardListData.Data,
            onItemClick: (RepresentCardListData.Data) -> Unit?
        ) {
            with(data) {

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
        holder.onBind(data, onItemClick)
    }

    private class MainCardComparator : DiffUtil.ItemCallback<RepresentCardListData.Data>() {
        override fun areItemsTheSame(
            oldItem: RepresentCardListData.Data,
            newItem: RepresentCardListData.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RepresentCardListData.Data,
            newItem: RepresentCardListData.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

}