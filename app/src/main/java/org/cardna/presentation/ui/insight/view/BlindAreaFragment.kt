package org.cardna.presentation.ui.insight.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cardna.R
import com.example.cardna.databinding.FragmentBlindAreaBinding
import com.example.cardna.databinding.FragmentOpenAreaBinding
import org.cardna.presentation.base.BaseViewUtil

class BlindAreaFragment : BaseViewUtil.BaseFragment<FragmentBlindAreaBinding>(R.layout.fragment_blind_area) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}