package org.cardna.presentation.ui.detailcard.view

import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityDetailCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil

@AndroidEntryPoint
class DetailCardActivity : BaseViewUtil.BaseAppCompatActivity<ActivityDetailCardBinding>(R.layout.activity_detail_card) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }

/*    putextra로 넘어온다
    id값으로 data얻어온다 ->data뿌리고 xml이 바인딩함

    likecount가 null이아니면->내가 내 카드를 본다
    1. 카드나: type=카드나
    2. 카드너: type=카드너

    likecount가 null이면->타인이 내 카드를 본다
    1. 카드나: type=카드나
    2. 카드너: type=카드너

    type이 보관함이면
    이에따라 레이아웃 수정정



    //커스텀 binding에 잇는 애들 클ㄹ릭 리스너 달기
    1.내가 카드나
    2.내가 카드너
    3.남이 카드나
    4.남이 카드너
    5.보관함함*/
}