package org.cardna.presentation.ui.maincard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cardna.R
import com.example.cardna.databinding.FragmentMainCardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel

@AndroidEntryPoint
class MainCardFragment :
    BaseViewUtil.BaseFragment<FragmentMainCardBinding>(R.layout.fragment_main_card) {
    private lateinit var mainCardAdapter: MainCardAdapter
    private val mainCardViewModel: MainCardViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainCardViewModel = mainCardViewModel
        initView()
    }

    override fun initView() {
        initData()
        setObserver()
        initAdapter()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    //onResume 에 뿌려질 데이터
    private fun initData() {
        mainCardViewModel.getMainCardList()
    }

    //adapter 관련 모음
    private fun initAdapter() {
        mainCardAdapter = MainCardAdapter()
        with(binding) {
            vpMaincardList.adapter = mainCardAdapter
        }
    }

    //click listener
    private fun setClickListener() {
        setEditCardActivity()
        setAlarmActivity()
    }

    private fun setObserver() {
//        mainCardTitleObserve()
//        mainCardCountObserve()
    }

    private fun setEditCardActivity() {
        binding.llMaincardEditLayout.setOnClickListener {
//            val intent = Intent(requireActivity(),EditCardActivity::class.java)
//            startActivity(intent)
        }
    }

//    private fun mainCardTitleObserve() {
//        mainCardViewModel.title.observe(viewLifecycleOwner) {
//            binding.tvMaincardTitle.text = it.toString()
//        }
//    }

//    private fun mainCardCountObserve() {
//        mainCardViewModel.mainOrder.observe(viewLifecycleOwner) {
//            binding.tvMaincardPageCount.text = it.toString()
//        }
//    }

    private fun setAlarmActivity() {
        binding.ibtnMaincardAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), AlarmActivity::class.java)
            startActivity(intent)
        }
    }
}
