package org.cardna.presentation.util

import org.cardna.data.remote.model.card.MainCard

interface ItemTouchHelperListener {
    fun onItemMove(from_position: Int, to_position: Int): Boolean
    fun onItemSwipe(position: Int)
}