package org.cardna.presentation.ui.editcard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ItemEditCardDialogBinding
import org.cardna.data.remote.model.card.CardData
import org.cardna.presentation.ui.editcard.viewmodel.EditCardViewModel

class EditCardDialogAdapter(
    val lifecycleOwner: LifecycleOwner,
    val editCardViewModel: EditCardViewModel
) :
    ListAdapter<CardData, EditCardDialogAdapter.ViewHolder>(EditCardDialogComparator()) {

    inner class ViewHolder(private val binding: ItemEditCardDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(data: CardData) {
            with(binding) {
                Glide
                    .with(itemView.context)
                    .load(data.cardImg)
                    .into(ivCardpackRecyclerview)
                tvEditcarddialogTitle.text = data.title
                clRvItem.setBackgroundResource(setBackground(data.isMe))
            }

            editCardViewModel.selectedCardList.observe(lifecycleOwner) { selectedCardList ->
                for (list in selectedCardList) {
                    if (list == data.id) {
                        binding.tvRepresentcardCount.text =
                            (selectedCardList.indexOf(data.id) + 1).toString()
                        binding.clEditcarddialogCount.visibility = View.VISIBLE
                    }
                }
            }

//            binding.clEditcarddialogCount.apply {
//                editCardViewModel.selectedCardList.observe(lifecycleOwner) { selectedCardList ->
//                    itemView.setOnClickListener {
//                        visibility =
//                                //선택안된애면 선택&&7개미만일때만
//                            if (visibility == View.INVISIBLE && selectedCardList.size < 7) {
//                                //선택안된애면 선택해서 추가
//                                editCardViewModel.setAddCard(data.id)
//                                //가장 마지막에 추가되는거니까 리스트의 마지막 사이즈
//                                binding.tvRepresentcardCount.text = selectedCardList.size.toString()
//                                View.VISIBLE
//                            } else {
//                                if (visibility == View.VISIBLE) { //이미 선택된 애면 선택해제
//                                    editCardViewModel.setDeleteCard(data.id)
//                                }
//                                View.INVISIBLE
//                            }
//                    }
//                }
//            }
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
            R.drawable.bg_main_green_stoke_black_radius_8
        } else {
            R.drawable.bg_main_purple_stoke_black_radius_8
        }
    }

}