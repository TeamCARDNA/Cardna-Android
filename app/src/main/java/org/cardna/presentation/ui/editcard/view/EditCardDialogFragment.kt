package org.cardna.presentation.ui.editcard.view

import android.os.Bundle
import android.view.View
import com.example.cardna.R
import com.example.cardna.databinding.FragmentEditCardDialogBinding
import org.cardna.presentation.base.BaseViewUtil

class EditCardDialogFragment(private val userId: Int) :
    BaseViewUtil.BaseFragment<FragmentEditCardDialogBinding>(R.layout.fragment_edit_card_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {

    }
}