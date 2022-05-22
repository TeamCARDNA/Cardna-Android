package org.cardna.presentation.ui.cardpack.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomImageLamdaData(
    val BottomImageListener: () -> (Unit)
) : Parcelable
