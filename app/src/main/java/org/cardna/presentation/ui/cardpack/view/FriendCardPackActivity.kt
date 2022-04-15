package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.cardna.R
import com.example.cardna.databinding.ActivityFriendCardPackBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel


class FriendCardPackActivity : BaseViewUtil.BaseAppCompatActivity<ActivityFriendCardPackBinding>(R.layout.activity_friend_card_pack) {

    private var id:Int? = 0
    lateinit var name:String

    private val cardPackViewModel: CardPackViewModel by viewModels()
    // 이러면 이는 FriendCardPackActivity 안에서 생성된 CardPackFragment 와 CardMeFragment, CardYouFragment의 viewModel과 같은 인스턴스가 되는 것
    // 따라서, intent로 받은 id와 name을 이 viewModel 안의 id와 name 프로퍼티에 넣어주고,
    // 이를 이용해서 updateCardMeList, updateCardYouList 를 해주면, 알아서 그 친구의 카드나, 카드너가 채워질 것.
    // 이를 이 Activity의 initView() 같은 메서드에서 해주면 되지 않을까

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        // 메인 Activity의 타인의 대표카드 Fragment에서 id, name을 intent로 넘겨주면
        // 이를 viewModel 안의 id와 name 프로퍼티에 넣어준다.
        cardPackViewModel.setUserId(intent.getIntExtra("id", 0))
        cardPackViewModel.setUserName(intent.getStringExtra("name"))

        // 그 친구의 id와 name을 바탕으로 cardMeList, cardYouList 업데이트
        cardPackViewModel.updateCardMeList()
        cardPackViewModel.updateCardYouList()

        // 이를 현재 FriendCardPackActivity에서 받아서 xx님의 카드팩으로 수정
        binding.tvFriendCardpackTitle.text = "$name" + "님의 카드팩"

        // 친구의 카드팩 프래그먼트 생성 후, Fcv에 띄우기
        val cardPackFragment = CardPackFragment()
        cardPackFragment.arguments = Bundle().apply {
            putInt("id", id!!)
            putString("name", name)
        }
        supportFragmentManager.beginTransaction().add(R.id.fcv_friend_card_me_you, cardPackFragment).commit() // add 쓰는게 맞나
    }

    fun makeCardYou() { // xml파일에서 iv_make_card_you 눌렀을 때 onClick 설정해주었음.
        val intent = Intent(this, CardCreateActivity::class.java).apply {
            putExtra("id", id)
            putExtra("name", name)
            putExtra("isCardMeOrYou", false) // 내 카드나 작성 or 친구 카드너 작성 인지도 넘겨줘야할
            putExtra("isCardPackOrMainCard" ,true) // 친구 카드팩에서부터 시작됐다는 것을 알려줌
        }
        startActivity(intent)
    }
}