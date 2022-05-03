package org.cardna.presentation.ui.cardpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.databinding.ItemCardpackCardyouBinding
import org.cardna.data.remote.model.card.ResponseCardYouData

class CardPackYouRecyclerViewAdapter( // CardYou에 대한 Adapter 로 변수명 변경했는데, 안된 것 있을수도 있으니 확인하기
    private val clickListener: ((ResponseCardYouData.CardList.CardYou) -> Unit)? = null,
) : ListAdapter<ResponseCardYouData.CardList.CardYou, CardPackYouRecyclerViewAdapter.CardPackYouViewHolder>(CardYouComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardPackYouViewHolder {
        val binding = ItemCardpackCardyouBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardPackYouViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardPackYouViewHolder, position: Int) {
        val cardYou: ResponseCardYouData.CardList.CardYou = getItem(position)
        holder.onBind(cardYou, clickListener)
    }


    class CardPackYouViewHolder(
        private val binding: ItemCardpackCardyouBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun onBind(cardYou: ResponseCardYouData.CardList.CardYou, onCardYouClick: ((ResponseCardYouData.CardList.CardYou) -> Unit) ?= null) {
            with(binding){
                Glide.with(itemView.context).load(cardYou.cardImg).into(binding.ivCardpackRecyclerview)
                tvCardpackRecyclerview.text = cardYou.title
                root.setOnClickListener{
                    onCardYouClick?.invoke(cardYou)
                }

                // 타인의 카드나이면, 공감버튼 선택하는 리스너도 달아줘야함
                // 애초에 item_cardpack_cardme xml 파일에 공감버튼을 추가하고, id가 null이면 이를 gone 시키고,
                // 이에 대해 리스너도 달아줘야함
            }
        }
    }

    private class CardYouComparator: DiffUtil.ItemCallback<ResponseCardYouData.CardList.CardYou>(){
        override fun areItemsTheSame(
            oldItem: ResponseCardYouData.CardList.CardYou,
            newItem: ResponseCardYouData.CardList.CardYou
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseCardYouData.CardList.CardYou,
            newItem: ResponseCardYouData.CardList.CardYou
        ): Boolean {
            return oldItem == newItem
        }
    }
}