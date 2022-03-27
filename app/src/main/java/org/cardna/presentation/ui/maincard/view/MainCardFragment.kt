package org.cardna.presentation.ui.maincard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.cardna.R
import com.example.cardna.databinding.FragmentMainCardBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity

@AndroidEntryPoint
class MainCardFragment : BaseViewUtil.BaseFragment<FragmentMainCardBinding>(R.layout.fragment_main_card) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        binding.btn.setOnClickListener{
            Intent(requireContext(), DetailCardActivity::class.java).apply {
                putExtra("id",277)
                startActivity(this)
            }
        }
    }
}