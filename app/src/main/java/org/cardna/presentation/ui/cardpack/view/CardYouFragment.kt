package org.cardna.presentation.ui.cardpack.view

import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentCardYouBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel


class CardYouFragment : BaseViewUtil.BaseFragment<FragmentCardYouBinding>(R.layout.fragment_card_you) {

    private val cardPackViewModel: CardPackViewModel by activityViewModels() // id, name

    override fun initView() {
    }


    // 친구의 카드너이면 공감버튼 추가해줘야함
}