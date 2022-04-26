package org.cardna.presentation.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.cardna.data.remote.model.card.MainCard

class ItemTouchHelperCallback(
    private val listener: ItemTouchHelperListener
) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (recyclerView.layoutManager is GridLayoutManager) {
            //GridLayout 형식
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            // drag 만 -> dragFlags, 0
            // swipe 만 -> 0, swipeFlags
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            //LinearLayout 형식
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.adapterPosition)
    }
}