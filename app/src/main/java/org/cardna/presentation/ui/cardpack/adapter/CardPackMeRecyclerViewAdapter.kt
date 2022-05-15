package org.cardna.presentation.ui.cardpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cardna.databinding.ItemCardpackCardmeBinding
import org.cardna.data.remote.model.card.ResponseCardMeData
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import org.cardna.presentation.util.showLottie
import timber.log.Timber

class CardPackMeRecyclerViewAdapter(
    // naming Me 빼서 수정 필요
    private val cardPackViewModel: CardPackViewModel,
    private val context: LifecycleOwner,
    private val clickListener: ((ResponseCardMeData.CardList.CardMe) -> Unit)? = null,
) : ListAdapter<ResponseCardMeData.CardList.CardMe, CardPackMeRecyclerViewAdapter.CardPackMeViewHolder>(CardMeComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardPackMeViewHolder {
        val binding = ItemCardpackCardmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardPackMeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardPackMeViewHolder, position: Int) {
        val cardMe: ResponseCardMeData.CardList.CardMe = getItem(position)
        holder.onBind(cardMe, clickListener)
    }


    inner class CardPackMeViewHolder(
        private val binding: ItemCardpackCardmeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(cardMe: ResponseCardMeData.CardList.CardMe, onCardMeClick: ((ResponseCardMeData.CardList.CardMe) -> Unit)? = null) {
            with(binding) {
                Glide.with(itemView.context).load(cardMe.cardImg).into(binding.ivCardpackRecyclerview)
                tvCardpackRecyclerview.text = cardMe.title
                root.setOnClickListener {
                    onCardMeClick?.invoke(cardMe)
                }
                Timber.e("CardMe onBind")
                // 타인의 카드나이면, 공감버튼 선택하는 리스너도 달아줘야함
                // 애초에 item_cardpack_cardme xml 파일에 공감버튼을 추가하고, id가 null이면 이를 gone 시키고,
                // 이에 대해 리스너도 달아줘야함

                cardPackViewModel.cardMeList.observe(context) { cardMeList ->
                    for (item in cardMeList) {
                        if (item.id == cardMe.id && item.isLiked == true)
                            ctvCardpackCardme.isChecked = true
                    }
                }
                if (cardPackViewModel.id != null) {
                    ctvCardpackCardme.setOnClickListener {
                        ctvCardpackCardme.toggle()
                        showLottie(binding.laCardpackCardyouLottie, DetailCardActivity.CARD_ME, "lottie_cardme.json")
                        cardPackViewModel.postLike(cardMe.id)
                    }
                }else{
                    ctvCardpackCardme.isChecked=true
                    ctvCardpackCardme.isClickable=false
                }
            }
        }
    }

    private class CardMeComparator : DiffUtil.ItemCallback<ResponseCardMeData.CardList.CardMe>() {
        override fun areItemsTheSame(
            oldItem: ResponseCardMeData.CardList.CardMe,
            newItem: ResponseCardMeData.CardList.CardMe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseCardMeData.CardList.CardMe,
            newItem: ResponseCardMeData.CardList.CardMe
        ): Boolean {
            return oldItem == newItem
        }
    }
}