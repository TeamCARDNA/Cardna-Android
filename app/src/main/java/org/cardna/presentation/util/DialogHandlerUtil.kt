package org.cardna.presentation.util

import android.app.Dialog
import android.os.Handler
import android.os.Looper

 fun setHandler(dialog: Dialog) {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({ dialog.dismiss() }, 150)
}