package org.cardna.presentation.ui.maincard.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cardna.R
import com.example.cardna.databinding.DialogMainCardBlockBinding
import com.example.cardna.databinding.FragmentMainCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.editcard.EditCardActivity
import org.cardna.presentation.ui.maincard.adapter.MainCardAdapter
import org.cardna.presentation.ui.maincard.viewmodel.MainCardViewModel
import timber.log.Timber
import kotlin.math.roundToInt

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
        initAdapter()
        setClickListener()
        userBlockCheck()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    //뿌려질 데이터
    private fun initData() {
        binding.mainCardViewModel = mainCardViewModel
        mainCardViewModel.getMainCardList()
        mainCardViewModel.getMyPageUser()
        setInitPagePosition()
        binding.vpMaincardList.setCurrentItem(mainCardViewModel.cardPosition.value ?: 0, false)
    }

    //adapter 관련 모음
    private fun initAdapter() {
        Timber.d("init adapter")
        val cardId = mainCardViewModel.cardId.value
        mainCardAdapter = MainCardAdapter(cardId)
        mainCardViewModel.cardList.observe(viewLifecycleOwner) {
            mainCardAdapter.submitList(it)
        }
        with(binding.vpMaincardList) {
            adapter = mainCardAdapter
            viewPagerAnimation()
        }
    }

    private fun viewPagerAnimation() {
        val compositePageTransformer = getPageTransformer()
        with(binding.vpMaincardList) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            setPageTransformer(compositePageTransformer)
            setPadding(
                (56 * resources.displayMetrics.density).roundToInt(),
                0,
                (56 * resources.displayMetrics.density).roundToInt(),
                0
            )
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun getPageTransformer(): ViewPager2.PageTransformer {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * resources.displayMetrics.density).roundToInt()))

        return compositePageTransformer
    }

    //click listener
    private fun setClickListener() {
        setEditCardActivity()
        setAlarmActivity()
    }

    private fun setEditCardActivity() {
        binding.llMaincardEditLayout.setOnClickListener {
            val intent = Intent(requireActivity(), EditCardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAlarmActivity() {
        binding.ibtnMaincardAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), AlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userBlockCheck() {
        val isBlock = mainCardViewModel.isBlocked.value
        if (isBlock == true) {
            val dialog = Dialog(requireActivity())
            val dialogBinding = DialogMainCardBlockBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun setInitPagePosition() {
        binding.vpMaincardList.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mainCardViewModel.saveInitCardPosition(position)
            }
        })
    }
}
