package org.cardna.presentation.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.DetailCardLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.ui.detailcard.viewmodel.DetailCardViewModel
import org.cardna.presentation.ui.insight.viewmodel.InsightViewModel

@AndroidEntryPoint
class DetailCardLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val detailCardViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<DetailCardViewModel>()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
       // viewModel.summaryModel.observe(findViewTreeLifecycleOwner()!!, ::populateSummaryView)
    }

    private val binding: DetailCardLayoutBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.detail_card_layout,
        this,
        true
    )

    @get:JvmName("text")
    @set:JvmName("text")
    var text: String
        get() = getText()
        set(value) = setText(value)

    init {
        //미리보기 느낌인가
        addView(binding.root)

        //타이틀, 이미지 세팅
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.DetailCardLayout).apply {
                val title = getString(R.styleable.DetailCardLayout_title)
                val imageUrl = getString(R.styleable.DetailCardLayout_imageUrl)

                with(binding) {
                    tvDetailcardTitle.text = title
                    Glide.with(context).load(imageUrl)
                        .into(binding.ivDetailcardImage)
                }
            }
        }
    }

    private fun initLayout(){



    }

    private fun getText() = binding.tvDetailcardTitle.text.toString()
    private fun setText(text: String) {
        binding.tvDetailcardTitle.setText(text)
    }
}