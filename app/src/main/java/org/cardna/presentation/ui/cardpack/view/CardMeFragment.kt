package org.cardna.presentation.ui.cardpack.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardMeBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel

class CardMeFragment : BaseViewUtil.BaseFragment<FragmentCardMeBinding>(R.layout.fragment_card_me) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name
    private var isMyCard: Boolean = true // 유저 본인의 카드나에 접근하는건지, 타인의 카드나에 접근하는 건지 이는 나중에


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        getCardMe()
    }


    private fun getCardMe(){
        if(cardPackViewModel.id == null){ // 본인의 카드나 접근

            // viewModel에 정의된
        }
        else{ // 타인의 카드나 접근

            // 뷰에 공감버튼 추가해줘야함
        }

    }
}