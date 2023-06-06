package com.example.tetris

import android.view.MotionEvent
import android.view.View

class OnTouchOnDeTouchListener(
    private val onTouchAction: () -> Unit,
    private val onDeTouchAction: () -> Unit,
) : View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                onTouchAction.invoke()
                return true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                onDeTouchAction.invoke()
                return true
            }
        }
        return false
    }
}
