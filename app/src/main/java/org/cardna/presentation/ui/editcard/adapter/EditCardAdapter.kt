package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amplitude.api.Amplitude
import com.bumptech.glide.Glide
import org.cardna.R
import org.cardna.data.remote.model.card.MainCard
import org.cardna.databinding.ItemEditCardBinding
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel
import org.cardna.presentation.util.ItemTouchHelperListener
import java.util.*

class EditCardAdapter(
    val editCardViewModel: EditCardViewModel
) : ListAdapter<MainCard, EditCardAdapter.ViewHolder>(EditCardComparator()),
    ItemTouchHelperListener {
    var mutableList = mutableListOf<MainCard>()

    override fun onCurrentListChanged(
        previousList: MutableList<MainCard>,
        currentList: MutableList<MainCard>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        mutableList = currentList
    }

    inner class ViewHolder(private val binding: ItemEditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            editCardViewModel.setChangeSelectedList(currentList.map { it.id } as MutableList<Int>)
        }

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
                //대표카드 삭제->아이템 포지션 전달
                ivRepresentcardeditlistDelete.setOnClickListener {
                    setNewList(adapterPosition)
                }
                root.setOnClickListener {
                    Amplitude.getInstance().logEvent("MainCardEdit_Cardpack_Choice")
                }
            }
        }
    }

    private fun setNewList(adapterPosition: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(adapterPosition)
        editCardViewModel.setChangeSelectedList(newList.map { it.id } as MutableList<Int>)
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
        val mutableList = currentList.toMutableList()
        mutableList.removeAt(from_position)
        mutableList.add(to_position, getItem(from_position))
        notifyItemMoved(from_position, to_position)
        onCurrentListChanged(currentList.toMutableList(), mutableList)
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