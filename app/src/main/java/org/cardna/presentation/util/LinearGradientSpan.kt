package org.cardna.presentation.util

import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import androidx.annotation.ColorInt

class LinearGradientSpan(
    private val containingText: String,
    private val textToStyle: String,
    @ColorInt private val startColorInt: Int,
    @ColorInt private val endColorInt: Int
) : CharacterStyle(), UpdateAppearance {

    override fun updateDrawState(tp: TextPaint?) {
        tp ?: return

        var leadingWidth = 0f
        val indexOfTextToStyle = containingText.indexOf(textToStyle)
        if (!containingText.startsWith(textToStyle) && containingText != textToStyle) {
            leadingWidth = tp.measureText(containingText, 0, indexOfTextToStyle)
        }
        val gradientWidth = tp.measureText(containingText, indexOfTextToStyle,
            indexOfTextToStyle + textToStyle.length)

        tp.shader = LinearGradient(
            leadingWidth,
            0f,
            leadingWidth + gradientWidth,
            0f,
            startColorInt,
            endColorInt,
            Shader.TileMode.MIRROR
        )
    }
}