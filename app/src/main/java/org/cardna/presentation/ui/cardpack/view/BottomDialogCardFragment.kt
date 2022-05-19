package org.cardna.ui.cardpack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cardna.databinding.FragmentBottomDialogCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import land.sungbin.systemuicontroller.setNavigationBarColor
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.view.BottomCardLamdaData
import org.cardna.presentation.ui.cardpack.view.CardYouStoreActivity
import timber.log.Timber


class BottomDialogCardFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomDialogCardBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화되지 않았습니다")

    private lateinit var itemClick: (Boolean) -> Unit
    private lateinit var bottomCardLamdaData: BottomCardLamdaData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomDialogCardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setSystemBarsColor(Color.BLACK, false)
        this.setNavigationBarColor(Color.BLACK, false)
        makeCard()
        initRamda()
    }

    private fun initRamda(){
        Timber.e("Init 람다")
        bottomCardLamdaData = arguments?.getParcelable("BottomCard")!!
        itemClick = bottomCardLamdaData.BottomCardListener
    }


    private fun makeCard(){
        binding.clBottomdialogCardTop.setOnClickListener{
            itemClick(BaseViewUtil.CARD_ME)
            // 컴패니언 상수인 cardme = 1이라면 cardme를 activity에 정의된 함수의 인자값으로 넘겨서 그거에 따라서 실행
            dialog?.dismiss()
            // 일단 다이얼로그는 무조건 없어지긴 해야함
        }

        binding.clBottomdialogCardBottom.setOnClickListener{
            startActivity(Intent(requireContext(),CardYouStoreActivity::class.java))
       //     itemClick(BaseViewUtil.CARD_YOU)
            dialog?.dismiss()
        }
    }
}