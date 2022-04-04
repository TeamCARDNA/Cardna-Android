package org.cardna.presentation.ui.cardpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardna.databinding.ItemCardpackCardmeBinding
import org.cardna.data.remote.model.card.ResponseCardPackData

class CardPackMeRecyclerViewAdapter( // naming Me 빼서 수정 필요
    private val clickListener: ((ResponseCardPackData.CardList.Card) -> Unit)? = null,
) : ListAdapter<ResponseCardPackData.CardList.Card, CardPackMeRecyclerViewAdapter.CardPackMeViewHolder>(CardMeComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardPackMeViewHolder {
        val binding = ItemCardpackCardmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardPackMeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardPackMeViewHolder, position: Int) {
        val cardMe: ResponseCardPackData.CardList.Card = getItem(position)
        holder.onBind(cardMe, clickListener)
    }


    class CardPackMeViewHolder(
        private val binding: ItemCardpackCardmeBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun onBind(cardMe: ResponseCardPackData.CardList.Card, onCardMeClick: ((ResponseCardPackData.CardList.Card) -> Unit) ?= null) {
            with(binding){
                Glide.with(itemView.context).load(cardMe.cardImg).into(binding.ivCardpackRecyclerview)
                tvCardpackRecyclerview.text = cardMe.title
                root.setOnClickListener{
                    onCardMeClick?.invoke(cardMe)
                }

                // 타인의 카드나이면, 공감버튼 선택하는 리스너도 달아줘야함
                // 애초에 item_cardpack_cardme xml 파일에 공감버튼을 추가하고, id가 null이면 이를 gone 시키고,
                // 이에 대해 리스너도 달아줘야함
            }
        }
    }

    private class CardMeComparator: DiffUtil.ItemCallback<ResponseCardPackData.CardList.Card>(){
        override fun areItemsTheSame(
            oldItem: ResponseCardPackData.CardList.Card,
            newItem: ResponseCardPackData.CardList.Card
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseCardPackData.CardList.Card,
            newItem: ResponseCardPackData.CardList.Card
        ): Boolean {
            return oldItem == newItem
        }
    }
}