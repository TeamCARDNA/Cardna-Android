package org.cardna.presentation.ui.maincard.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.cardna.R
import com.example.cardna.databinding.FragmentMainCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel

@AndroidEntryPoint
class MainCardFragment :
    BaseViewUtil.BaseFragment<FragmentMainCardBinding>(R.layout.fragment_main_card) {
    private lateinit var mainCardAdapter: MainCardAdapter
    private val mainCardViewModel: MainCardViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        initData()

    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    //adapter 관련 모음
    private fun setMainCardAdapter() {

    }

    //click listener
    private fun setClickListener() {

    }

    //onResume 에 뿌려질 데이터
    private fun initData() {

    }

    private fun setObserver() {

    }

}