package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import org.cardna.R
import org.cardna.databinding.ActivityFriendCardPackBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.util.StatusBarUtil


class FriendCardPackActivity : BaseViewUtil.BaseAppCompatActivity<ActivityFriendCardPackBinding>(R.layout.activity_friend_card_pack) {

    private val cardPackViewModel: CardPackViewModel by viewModels()
    // 이러면 이는 FriendCardPackActivity 안에서 생성된 CardPackFragment 와 CardMeFragment, CardYouFragment 의 viewModel 과 같은 인스턴스가 되는 것
    // 따라서, intent 로 받은 id와 name 을 이 viewModel 안의 id와 name 프로퍼티에 넣어주고,
    // 이를 이용해서 updateCardMeList, updateCardYouList 를 해주면, 알아서 그 친구의 카드나, 카드너가 채워질 것.
    // 이를 이 Activity 의 initView() 같은 메서드에서 해주면 되지 않을까

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        StatusBarUtil.setStatusBar(this, Color.BLACK)
        // 메인 Activity 의 타인의 대표카드 Fragment 에서 id, name 을 intent로 넘겨주면
        // 이를 viewModel 안의 id와 name 프로퍼티에 넣어준다.
        cardPackViewModel.setUserId(intent.getIntExtra(BaseViewUtil.ID, 0))
        cardPackViewModel.setUserName(intent.getStringExtra(BaseViewUtil.NAME))

        // 그 친구의 id와 name 을 바탕으로 cardMeList, cardYouList 업데이트
        cardPackViewModel.updateCardMeList()
        cardPackViewModel.updateCardYouList()

        // 이를 현재 FriendCardPackActivity 에서 받아서 xx 님의 카드팩으로 수정
        binding.tvFriendCardpackTitle.text = "${cardPackViewModel.name}" + "님의 카드팩"

        // 친구의 카드팩 프래그먼트 생성 후, Fcv에 띄우기
        val cardPackFragment = CardPackFragment()
        cardPackFragment.arguments = Bundle().apply {
            putInt(BaseViewUtil.ID, cardPackViewModel.id!!)
            putString(BaseViewUtil.NAME, cardPackViewModel.name)
        }
        supportFragmentManager.beginTransaction().add(R.id.fcv_friend_card_me_you, cardPackFragment).commit() // add 쓰는게 맞나
    }

    fun makeCardYou() { // xml 파일에서 iv_make_card_you 눌렀을 때 onClick 설정해주었음.
        val intent = Intent(this, CardCreateActivity::class.java).apply {
            putExtra(BaseViewUtil.ID, cardPackViewModel.id)
            putExtra(BaseViewUtil.NAME, cardPackViewModel.name)
            putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_YOU) // 내 카드나 작성 or 친구 카드너 작성 인지도 넘겨줘야할
            putExtra(BaseViewUtil.IS_CARDPACK_OR_MAINCARD ,BaseViewUtil.FROM_CARDPACK) // 친구 카드팩에서부터 시작됐다는 것을 알려줌
        }
        startActivity(intent)
    }
}