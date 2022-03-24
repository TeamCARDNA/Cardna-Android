package org.cardna.presentation.util

import android.widget.CheckedTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel


@BindingAdapter("viewModel","reasonNumber")
fun setOnCheckedChanged(view: CheckedTextView, viewModel: ViewModel, reasonNumber: Int) {
    view.setOnClickListener {
        view.toggle()
        //TODO 서버연결 후 처리   (viewModel as? SettingViewModel)?.setSaveData()
    }
}