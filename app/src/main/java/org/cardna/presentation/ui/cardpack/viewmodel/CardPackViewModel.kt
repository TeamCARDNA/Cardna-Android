package org.cardna.presentation.ui.cardpack.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CardPackViewModel @Inject constructor(
) : ViewModel() { // CardPack, CardYou, CardMeFragment 가 이를 공유 ?

    // 어떤 id 의 사람의 카드팩 프래그먼트에 접근하는지
    private var _id:Int? = null
    val id: Int?
        get() = _id

    // 어떤 name 의 사람의 카드팩 프래그먼트에 접근하는지
    private var _name:String? = null
    val name: String?
        get() = _name

    // 일단 id를 null로 할당해놓고, 타인의 카드팩 접근할 때는 이를 타인의 id로 초기화해줘야 할듯.
    // 그 method 구현 필요 => 메인 액티비티 마이페이지 프래그먼트에서 친구의 id를 넘겨줘서 친구 액티비티 띄우고, 여기서
    // 카드팩 프래그먼트 생성할 때, 인자로 넘겨서 생성 ? 그러면 viewModel도 생성자에 id를 넘겨줘야하나

    fun setUserId(id: Int?){
        _id = id
    } // 타인의 프래그먼트 생성시, 그 프래그먼트 코드 단에서 getArguments로 받아온 newId를 setUserId(newId) 이런형식으로 설정 ?

    fun setUserName(name: String?){
        _name = name
    }
}