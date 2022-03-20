package org.cardna.presentation.ui.insight

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cardna.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightFragment : BaseViewUtil.BaseFragment<FragmentInsightBinding>(R.layout.fragment_insight) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
    }
}
