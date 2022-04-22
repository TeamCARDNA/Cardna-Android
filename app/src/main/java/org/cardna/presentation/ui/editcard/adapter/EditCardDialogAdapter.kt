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
import org.cardna.presentation.ui.editcard.viewmodel.EditCardDialogViewModel
import timber.log.Timber

class EditCardDialogAdapter(val editCardDialogViewModel: EditCardDialogViewModel) :
    ListAdapter<CardData, EditCardDialogAdapter.ViewHolder>(EditCardDialogComparator()) {
    private var lastRemovedIndex = 8
    private var itemClickListener: ((Int, CardData, Boolean) -> Int)? = null
    private val selectedList: MutableList<Int> =
        editCardDialogViewModel.selectedCardList.value as MutableList<Int>

    fun setItemClickListener(listener: ((Int, CardData, Boolean) -> Int)) {
        itemClickListener = listener
    }

    fun setLastRemovedIndex(index: Int) {
        lastRemovedIndex = index
    }

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

                val mainOrder = "${data.mainOrder}"
                if (mainOrder != "null") {
                    Timber.d("mainOrder : $mainOrder")
                    binding.tvRepresentcardCount.apply {
                        text = mainOrder.substring(0, 1)
                        selectedList.add(data.id)
                        editCardDialogViewModel.setChangeSelectedList(selectedList)
                        visibility = View.VISIBLE
                    }
                }

                itemView.setOnClickListener {
                    binding.tvRepresentcardCount.apply {
                        visibility =
                            if (visibility == View.GONE) {
                                selectedList.add(data.id)
                                text = selectedList.size.toString()
                                View.VISIBLE
                            } else {
                                selectedList.removeAt(selectedList.indexOf(data.id))
                                View.GONE
                            }
                    }
                    editCardDialogViewModel.setChangeSelectedList(selectedList)
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