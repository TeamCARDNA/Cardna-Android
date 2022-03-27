package org.cardna.presentation.util

import android.widget.CheckedTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import org.cardna.presentation.ui.setting.viewmodel.SettingViewModel


@BindingAdapter("viewModel", "reasonNumber")
fun setOnCheckedChanged(view: CheckedTextView, viewModel: ViewModel, reasonNumber: Int) {
    view.setOnClickListener {
        view.toggle()
        when (reasonNumber) {
            1 -> (viewModel as? SettingViewModel)?.setSecessionReasonOneStatus(view.isChecked)
            2 -> (viewModel as? SettingViewModel)?.setSecessionReasonTwoStatus(view.isChecked)
            3 -> (viewModel as? SettingViewModel)?.setSecessionReasonThreeStatus(view.isChecked)
            4 -> (viewModel as? SettingViewModel)?.setSecessionReasonFourStatus(view.isChecked)
            5 -> (viewModel as? SettingViewModel)?.setSecessionReasonFiveStatus(view.isChecked)
            6 -> (viewModel as? SettingViewModel)?.setSecessionReasonSixStatus(view.isChecked)
        }
    }
}
