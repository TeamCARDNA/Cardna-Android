package org.cardna.presentation.ui.cardpack.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardYouBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel


class CardYouFragment : BaseViewUtil.BaseFragment<FragmentCardYouBinding>(R.layout.fragment_card_you) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        getCardYou()
    }

    private fun getCardYou(){
        if(cardPackViewModel.id == null){ // 본인의 카드너 접근

            // viewModel에 정의된
        }
        else{ // 타인의 카드너 접근

            // 뷰에 공감버튼 추가해줘야함
        }

    }
}