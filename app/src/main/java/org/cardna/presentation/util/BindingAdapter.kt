package org.cardna.presentation.util

import android.app.Activity
import android.widget.CheckedTextView
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import org.cardna.presentation.ui.mypage.view.SearchFriendCodeActivity
import org.cardna.presentation.ui.mypage.viewmodel.MyPageViewModel
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

@BindingAdapter("viewModel", "activity", "relationNumber")
fun setOnCheckedChanged(view: AppCompatButton, viewModel: ViewModel, activity: Activity, relationNumber: Int) {
    view.setOnClickListener {
        when (relationNumber) {
            1 -> (viewModel as? MyPageViewModel)?.applyFriend() //친구신청  1->3
            2 -> (activity as? SearchFriendCodeActivity)?.showBreakUpFriendDialog() //친구끊기 2->1
            3 -> (activity as? SearchFriendCodeActivity)?.showCancelFriendRequestDialog() //요청3->요청취소1
        }
    }
}

@BindingAdapter("imgResId")
fun setImageResource(view: ImageView, resId: String) {
    if(resId.isNotEmpty())
    Glide.with(view.context)
        .load(resId)
        .into(view)
}

