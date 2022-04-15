package org.cardna.presentation.ui.cardpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cardna.databinding.ItemCardyoustoreBinding
import org.cardna.data.remote.model.card.ResponseCardYouStoreData

class CardYouStoreRecyclerViewAdapter(
    private var onCardClickListener: ((ResponseCardYouStoreData.Data) -> Unit)? = null
) : ListAdapter<ResponseCardYouStoreData.Data, CardYouStoreRecyclerViewAdapter.CardYouStoreRecyclerViewHolder>(CardYouStoreComparator()) {


    inner class CardYouStoreRecyclerViewHolder(private val binding: ItemCardyoustoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseCardYouStoreData.Data) {
            with(binding) {
                tvCardyoustoreTitle.text = data.title
                tvCardyoustoreName.text = data.name
                tvOtherwriteCreatedAt.text = data.createdAt
                ivCardyoustoreImage.isVisible = data.isImage
                onCardClickListener?.run { binding.root.setOnClickListener { this(data) } }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardYouStoreRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCardyoustoreBinding.inflate(layoutInflater, parent, false)
        return CardYouStoreRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardYouStoreRecyclerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class CardYouStoreComparator : DiffUtil.ItemCallback<ResponseCardYouStoreData.Data>() {
        override fun areItemsTheSame(oldItem: ResponseCardYouStoreData.Data, newItem: ResponseCardYouStoreData.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResponseCardYouStoreData.Data, newItem: ResponseCardYouStoreData.Data): Boolean {
            return oldItem == newItem
        }
    }
}