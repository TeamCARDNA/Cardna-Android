package org.cardna.presentation.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.DetailCardLayoutBinding

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
                val imageUrl = getString(R.styleable.DetailCardLayout_imageUrl)

                //타이틀이랑 이미지 세팅은 무조건 해줘야함
                with(binding) {
                    tvDetailcardTitle.text = title
                    Glide.with(context).load(imageUrl)
                        .into(binding.ivDetailcardImage)
                }

                //카드나/카드너->배경색 바껴야함

                //내가보냐

                //남이보냐

                //보관함이냐
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