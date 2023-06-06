package com.example.tetris

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View

class RepeatOnTouchListener(
    private val repeatDelay: Long,
    private val action: () -> Unit
): View.OnTouchListener {

    private val handler = Handler(Looper.getMainLooper())
    private var isPressed = false

    private val runnable = object : Runnable {
        override fun run() {
            if (isPressed) {
                action.invoke()
                handler.postDelayed(this, repeatDelay)
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                handler.removeCallbacks(runnable)
                handler.post(runnable)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isPressed = false
                handler.removeCallbacks(runnable)
                view?.performClick()
                return true
            }
        }
        return false
    }
}
