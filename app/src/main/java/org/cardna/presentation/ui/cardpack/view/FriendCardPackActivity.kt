package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityFriendCardPackBinding
import org.cardna.presentation.base.BaseViewUtil

class FriendCardPackActivity : BaseViewUtil.BaseAppCompatActivity<ActivityFriendCardPackBinding>(R.layout.activity_friend_card_pack) {

    private var id:Int = 0
    lateinit var name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        // 메인 페이지 Activity의 타인의 대표카드 Fragment에서 id, name을 intent로 넘겨주면
        // 이를 현재 FriendCardPackActivity에서 받아서 xx님의 카드팩으로 수정
        // 그리고 카드팩 Fragment 만들 때, 이를 arguments 로 넘겨줌.

        // 카드팩 프래그먼트 생성
    }

    fun makeCardYou() { // xml파일에서 iv_make_card_you 눌렀을 때 onClick 설정해주었음.
        val intent = Intent(this, CardCreateActivity::class.java).apply {
            putExtra("id", id)
            putExtra("name", name)
        }
        startActivity(intent)
    }
}