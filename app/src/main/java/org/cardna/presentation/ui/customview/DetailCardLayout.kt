package org.cardna.presentation.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.cardna.R
import com.example.cardna.databinding.DetailCardLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCardLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


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

        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.DetailCardLayout).apply {
                val title = getString(R.styleable.DetailCardLayout_title)

                with(binding) {
                    tvDetailcardTitle.text = title
                }
            }
        }
    }

    private fun getText() = binding.tvDetailcardTitle.text.toString()
    private fun setText(text: String) {
        binding.tvDetailcardTitle.text = text
    }
}