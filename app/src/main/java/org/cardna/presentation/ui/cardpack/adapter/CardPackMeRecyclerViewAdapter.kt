package org.cardna.presentation.ui.cardpack.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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


        fun onBind(
            cardMe: ResponseCardMeData.CardList.CardMe,
            onCardMeClick: ((ResponseCardMeData.CardList.CardMe) -> Unit)? = null,
            onCardMeLike: ((ResponseCardMeData.CardList.CardMe) -> Unit)? = null
        ) {
            with(binding) {
                // data 채워주기
                Glide.with(itemView.context).load(cardMe.cardImg).into(binding.ivCardpackCardmeRecyclerview)
                tvCardpackRecyclerview.text = cardMe.title

                // 리스너 달기
                root.setOnClickListener {
                    onCardMeClick?.invoke(cardMe)
                }


                cardPackViewModel.id.observe(context) {
                    if (it != null) {
                        ctvCardpackCardme.visibility = View.VISIBLE

                        cardPackViewModel.cardMeList.observe(context) { cardMeList ->
                            cardPackViewModel.id.observe(context) {
                                for (item in cardMeList) {
                                    if (item.id == cardMe.id) {
                                        ctvCardpackCardme.isChecked = cardMe.isLiked ?: false
                                    }
                                }
                            }
                        }

                        ctvCardpackCardme.setOnClickListener {
                            cardPackViewModel.postLike(cardMe.id)
                            ctvCardpackCardme.toggle()
                            if (ctvCardpackCardme.isChecked) {
                                showLottie(binding.laCardpackCardmeLottie, DetailCardActivity.CARD_ME, "lottie_cardme.json")
                            }
                        }
                    }
                }
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
