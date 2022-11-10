package org.cardna.ui.cardpack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amplitude.api.Amplitude
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import land.sungbin.systemuicontroller.setNavigationBarColor
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.databinding.FragmentBottomDialogCardBinding
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
        bottomCardLamdaData = arguments?.getParcelable(BaseViewUtil.BOTTOM_CARD)!!
        itemClick = bottomCardLamdaData.BottomCardListener
    }


    private fun makeCard(){
        binding.clBottomdialogCardTop.setOnClickListener{
            Amplitude.getInstance().logEvent("CardPack_WritingCardna")
            itemClick(BaseViewUtil.CARD_ME)
            dialog?.dismiss()
        }

        binding.clBottomdialogCardBottom.setOnClickListener{
            Amplitude.getInstance().logEvent("CardPack_PlusCardner")
            startActivity(Intent(requireContext(),CardYouStoreActivity::class.java))
            dialog?.dismiss()
        }
    }
}