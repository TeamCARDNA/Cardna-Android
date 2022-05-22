package org.cardna.presentation.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SpacesItemDecorationCardPack : RecyclerView.ItemDecoration() {
    fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val betweenSpacePx = BETWEEN_SPACE.toPx()
        val bottomSpacePx = BOTTOM_SPACE.toPx()
        val betweenMiddleSpacePx = BETWEEN_SPACE_MIDDLE.toPx()

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = betweenSpacePx
            outRect.right = betweenMiddleSpacePx
        } else {
            outRect.right = betweenSpacePx
            outRect.left =betweenMiddleSpacePx
        }
        outRect.bottom = bottomSpacePx
    }
    companion object {
        private const val BETWEEN_SPACE = 16
        private const val BOTTOM_SPACE = 16
        private const val BETWEEN_SPACE_MIDDLE = 8
    }
}

class SpacesItemDecorationHorizontalDialog : RecyclerView.ItemDecoration() {
    fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val betweenSpacePx = BETWEEN_SPACE.toPx()
        val bottomSpacePx = BOTTOM_SPACE.toPx()
        val betweenMiddleSpacePx = BETWEEN_SPACE_MIDDLE.toPx()

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = betweenSpacePx
            outRect.right = betweenMiddleSpacePx
        } else {
            outRect.right = betweenSpacePx
            outRect.left =betweenMiddleSpacePx
        }
        outRect.bottom = bottomSpacePx
    }
    companion object {
        private const val BETWEEN_SPACE = 16
        private const val BOTTOM_SPACE = 7
        private const val BETWEEN_SPACE_MIDDLE = 7
    }
}

class SpacesItemDecorationHorizontalActivity(private val space: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
    }
}

class MyPageItemVerticalDecoration : RecyclerView.ItemDecoration() {
    fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val betweenSpacePx = BETWEEN_SPACE.toPx()
        val bottomSpacePx = BOTTOM_SPACE.toPx()

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = betweenSpacePx
        } else {
            outRect.left = betweenSpacePx
        }
        outRect.bottom = bottomSpacePx
    }

    companion object {
        private const val BETWEEN_SPACE = 6
        private const val BOTTOM_SPACE = 12
    }
}