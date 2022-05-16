package org.cardna.presentation.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = space
        outRect.bottom = space
    }
}

class SpacesItemDecoration2(private val spaceRight: Int, private val spaceBottom: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = spaceRight
        outRect.bottom = spaceBottom
    }
}

class SpacesItemDecorationHorizontal :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = 6
        outRect.right = 6
        outRect.bottom = 12
    }
}

class SpacesItemDecorationOnlyBottom(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
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