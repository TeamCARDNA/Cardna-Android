package org.cardna.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.cardna.R
import com.example.cardna.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.view.CardCreateActivity
import org.cardna.presentation.ui.cardpack.view.CardPackFragment
import org.cardna.presentation.ui.insight.view.InsightFragment
import org.cardna.presentation.ui.maincard.view.MainCardFragment
import org.cardna.presentation.ui.mypage.view.MyPageFragment
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.replace
import org.cardna.ui.cardpack.BottomDialogCardFragment

@AndroidEntryPoint
class MainActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainCardFragment: MainCardFragment by lazy { MainCardFragment() }
    private val insightFragment: InsightFragment by lazy { InsightFragment() }
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initBottomNavigation()
        setBottomNavigationSelectListener()
    }

    private fun initBottomNavigation() {
        replace(R.id.fcv_main, mainCardFragment)
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_maincard -> {
                    supportFragmentManager.popBackStack()
                    StatusBarUtil.setStatusBar(this, Color.BLACK)
                    replace(R.id.fcv_main, mainCardFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_bottom_cardpack -> {
                    supportFragmentManager.popBackStack()
                    StatusBarUtil.setStatusBar(this, Color.BLACK)
                    replace(R.id.fcv_main,CardPackFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menu_bottom_insight -> {
                    supportFragmentManager.popBackStack()
                    StatusBarUtil.setStatusBar(this, Color.BLACK)
                    replace(R.id.fcv_main, insightFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager.popBackStack()
                    StatusBarUtil.setStatusBar(this, Color.BLACK)
                    replace(R.id.fcv_main, myPageFragment)
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    private fun setBottomNavigationSelectListener() {
        binding.bnvMain.itemIconTintList = null
        binding.bnvMain.selectedItemId = R.id.menu_bottom_maincard
    }

    // cardpackFragment의 버튼을 눌렀을 때, 이 MainActivity의 함수를 실행
    // 즉, MainActivity에서 BottomsheetDialog를 띄워주는 함수

    // 그럼 이 함수 FriendCardPackActivity에서도 리스너 달아줘야 할 듯듯
    fun showBottomDialogCardFragment() {
        // 바텀싯 다이얼로그가 뜬 후, 카드나 or 카드너를 선택했을 때, 그거에 따라 어떤 액티비티를 띄워줘야 하는지를 명세한 Fragment 정의하고
        val bottomDialogCardFragment = BottomDialogCardFragment {
            when (it) {
                CARD_ME -> {
                    val intent = Intent(this, CardCreateActivity::class.java)
                    startActivity(intent)
                }
                CARD_YOU -> {
                    // 카드너임을 알 수 있도록 intent에 추가정보 넣어줘야함
                    val intent = Intent(this, CardCreateActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        bottomDialogCardFragment.show(supportFragmentManager, bottomDialogCardFragment.tag)
    }

    companion object {
        const val CARD_ME = 0
        const val CARD_YOU = 1
    }
}