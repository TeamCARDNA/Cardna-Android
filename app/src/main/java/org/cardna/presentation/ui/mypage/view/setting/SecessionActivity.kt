package org.cardna.presentation.ui.mypage.view.setting

import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import androidx.activity.viewModels
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySecessionBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.mypage.viewmodel.SettingViewModel

@AndroidEntryPoint
class SecessionActivity : BaseViewUtil.BaseAppCompatActivity<ActivitySecessionBinding>(R.layout.activity_secession) {
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.secessionActivity = this
        binding.settingViewModel = settingViewModel
        initView()
    }

    override fun initView() {

    }

    private fun setObserve() {
        //TODO 6번 이유 TRUE시 VIEW보이기
       // binding.etSecessionReason.visibility = View.INVISIBLE
        //TODO 이유 하나라도 false없으면 탈퇴하기 버튼 클릭되게하기
    }

    companion object {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
    }
}