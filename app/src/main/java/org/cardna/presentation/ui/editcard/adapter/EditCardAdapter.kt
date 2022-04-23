package org.cardna.presentation.ui.editcard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardBinding
import org.cardna.data.remote.model.card.MainCard
import org.cardna.presentation.ui.editcard.viewmodel.EditCardDialogViewModel
import org.cardna.presentation.util.ItemTouchHelperListener
import timber.log.Timber

class EditCardAdapter(
    val editCardDialogViewModel: EditCardDialogViewModel
) : ListAdapter<MainCard, EditCardAdapter.ViewHolder>(EditCardComparator()),
    ItemTouchHelperListener {
    inner class ViewHolder(private val binding: ItemEditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            editCardDialogViewModel.setChangeSelectedList(currentList.map { it.id } as MutableList<Int>)
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
            }
        }
    }

    private fun setNewList(adapterPosition: Int) {
        val newList = currentList.toMutableList()  //현재 리스트 복사한다음에
        newList.removeAt(adapterPosition) //지우려고 선택한 아이템을 현재 리스트에서 지우고
        //삭제할거하고 남은 대표카드 수정에 있는 카드의 id만 남겨서 뷰모델한테 전달
        editCardDialogViewModel.setChangeSelectedList(newList.map { it.id } as MutableList<Int>)
        submitList(newList) //삭제한거 제거한 newlist를 리사이클러뷰에 다시 뿌림
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