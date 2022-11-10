package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.R
import org.cardna.databinding.ActivityFriendCardPackBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardPackViewModel
import org.cardna.presentation.util.StatusBarUtil
import timber.log.Timber

@AndroidEntryPoint
class FriendCardPackActivity : BaseViewUtil.BaseAppCompatActivity<ActivityFriendCardPackBinding>(R.layout.activity_friend_card_pack) {

    private val cardPackViewModel: CardPackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        cardPackViewModel.setUserId(intent.getIntExtra(BaseViewUtil.ID, 0))
        cardPackViewModel.setUserName(intent.getStringExtra(BaseViewUtil.NAME))
        cardPackViewModel.updateCardMeList()
        cardPackViewModel.updateCardYouList()
        makeCardYou()
        binding.tvFriendCardpackTitle.text = "${cardPackViewModel.name}" + "님의 카드팩"

        val cardPackFragment = CardPackFragment()
        cardPackFragment.arguments = Bundle().apply {
            putInt(BaseViewUtil.ID, cardPackViewModel.id.value ?: return)
            putString(BaseViewUtil.NAME, cardPackViewModel.name)
        }
        supportFragmentManager.beginTransaction().add(R.id.fcv_friend_card_me_you, cardPackFragment).commit()
    }

    private fun makeCardYou() {
        binding.ctlMakeCardYou.setOnClickListener {
            val intent = Intent(this, CardCreateActivity::class.java).apply {
                putExtra(BaseViewUtil.ID, cardPackViewModel.id.value)
                putExtra(BaseViewUtil.NAME, cardPackViewModel.name)
                putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_YOU) // 내 카드나 작성 or 친구 카드너 작성 인지도 넘겨줘야할
                putExtra(BaseViewUtil.IS_CARDPACK_OR_MAINCARD, BaseViewUtil.FROM_CARDPACK) // 친구 카드팩에서부터 시작됐다는 것을 알려줌
            }
            startActivity(intent)
        }
    }
}