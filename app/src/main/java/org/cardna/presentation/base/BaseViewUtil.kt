package org.cardna.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.cardna.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

sealed class BaseViewUtil {
    abstract class BaseAppCompatActivity<T : ViewDataBinding>(@LayoutRes val layoutRes: Int) :
        AppCompatActivity() {
        protected lateinit var binding: T

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, layoutRes)
            binding.lifecycleOwner = this
        }

        abstract fun initView()
    }

    abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layout: Int) : Fragment() {
        private var _binding: T? = null
        protected val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = DataBindingUtil.inflate(inflater, layout, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        abstract fun initView()
    }

    abstract class BaseBottomDialogFragment<T : ViewDataBinding>(@LayoutRes val layout: Int) :
        BottomSheetDialogFragment() {
        private var _binding: T? = null
        protected val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = DataBindingUtil.inflate(inflater, layout, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return BottomSheetDialog(requireContext(), R.style.BottomDialog)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        abstract fun initView()
    }

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val CARD_ID = "CARDID" // CardPackFragment => DetailActivity

        const val SYMBOL_ID = "symbolId"
        const val CARD_IMG = "cardImg"
        const val CARD_TITLE = "cardTitle"

        const val IS_CARDPACK_OR_MAINCARD = "isCardPackOrMainCard"
        const val FROM_CARDPACK = true
        const val FROM_MAINCARD = false

        // 1. MainActivity ~ BottomDialogImage
        const val IS_CARD_ME_OR_YOU = "isCardMeOrYou"
        const val CARD_ME = true
        const val CARD_YOU = false

        // BottomDialogImageFragment
        const val SYMBOL_0 = 1
        const val SYMBOL_1 = 2
        const val SYMBOL_2 = 3
        const val SYMBOL_3 = 4
        const val SYMBOL_4 = 5
        const val GALLERY = 6
    }
}