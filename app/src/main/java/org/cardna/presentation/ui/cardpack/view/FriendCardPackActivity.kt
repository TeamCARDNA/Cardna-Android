package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityFriendCardPackBinding
import org.cardna.data.remote.model.card.ResponseCardPackData
import org.cardna.presentation.base.BaseViewUtil

class FriendCardPackActivity : BaseViewUtil.BaseAppCompatActivity<ActivityFriendCardPackBinding>(R.layout.activity_friend_card_pack) {

    private var id:Int? = 0
    lateinit var name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        // 메인 Activity의 타인의 대표카드 Fragment에서 id, name을 intent로 넘겨주면
        id = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")!!

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
            putExtra("isCardMe", false) // 내 카드나 작성 or 친구 카드너 작성 인지도 넘겨줘야할 듯
        }
        startActivity(intent)
    }
}