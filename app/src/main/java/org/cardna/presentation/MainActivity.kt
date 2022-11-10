package org.cardna.presentation

import android.content.Intent
import android.os.Bundle
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.R
import org.cardna.databinding.ActivityMainBinding
import org.cardna.domain.repository.CardRepository
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.cardpack.view.BottomCardLamdaData
import org.cardna.presentation.ui.cardpack.view.CardCreateActivity
import org.cardna.presentation.ui.cardpack.view.CardPackFragment
import org.cardna.presentation.ui.cardpack.view.CardYouStoreActivity
import org.cardna.presentation.ui.insight.view.InsightFragment
import org.cardna.presentation.ui.login.view.SetNameFinishedActivity
import org.cardna.presentation.ui.maincard.view.MainCardFragment
import org.cardna.presentation.ui.mypage.view.MyPageFragment
import org.cardna.presentation.util.replace
import org.cardna.ui.cardpack.BottomDialogCardFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainCardFragment: MainCardFragment by lazy { MainCardFragment() }
    private val insightFragment: InsightFragment by lazy { InsightFragment() }
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }
    private val cardPackFragment: CardPackFragment by lazy {CardPackFragment() }

    @Inject
    lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initGetIntent()
        initBottomNavigation()
        setBottomNavigationSelectListener()
    }

    private fun initBottomNavigation() {
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_maincard -> {
                    supportFragmentManager.popBackStack()
                    replace(R.id.fcv_main, mainCardFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_bottom_cardpack -> {
                    supportFragmentManager.popBackStack()
                    replace(R.id.fcv_main, CardPackFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menu_bottom_insight -> {
                    supportFragmentManager.popBackStack()
                    replace(R.id.fcv_main, insightFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager.popBackStack()
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

    fun showBottomDialogCardFragment() {
        val itemClick : (Boolean) -> Unit  =
            {it ->
                when (it) {
                    BaseViewUtil.CARD_ME -> {
                        val intent = Intent(this, CardCreateActivity::class.java).apply {
                            putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, true)
                        }
                        startActivity(intent)
                    }
                    BaseViewUtil.CARD_YOU -> {
                        val intent = Intent(this, CardYouStoreActivity::class.java).apply {
                            putExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, false)
                        }
                        startActivity(intent)
                    }
                }
            }

        val bottomDialogCardFragment = BottomDialogCardFragment()
        bottomDialogCardFragment.arguments = Bundle().apply {
            putParcelable(
                BaseViewUtil.BOTTOM_CARD, BottomCardLamdaData(itemClick)
            )
        }

        if(supportFragmentManager.findFragmentByTag(bottomDialogCardFragment.tag) == null) {
            bottomDialogCardFragment.show(supportFragmentManager, bottomDialogCardFragment.tag)
        }
    }

    private fun initGetIntent() {

        val dynamicLinkData = intent.extras

        intent.getStringExtra(SetNameFinishedActivity.GO_TO_CARDCREAT_ACTIVITY_KEY)?.let {
            replace(R.id.fcv_main, mainCardFragment)
            startActivity(Intent(this, CardCreateActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(SetNameFinishedActivity.GO_TO_CARDCREAT_ACTIVITY_KEY, SetNameFinishedActivity.GO_TO_CARDCREAT_ACTIVITY)
            })
        } ?: replace(R.id.fcv_main, mainCardFragment)

        if (dynamicLinkData != null) {
            Amplitude.getInstance().logEvent("Alarm_from_PushAlarm")
            if (dynamicLinkData.get("body").toString().contains("작성")) {
                startActivity(Intent(this, AlarmActivity::class.java).apply {
                })
            } else if (dynamicLinkData.get("body").toString().contains("친구")) {
                startActivity(Intent(this, AlarmActivity::class.java).apply {
                })
            }
        }
    }
}