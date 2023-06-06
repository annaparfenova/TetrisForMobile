package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val borderPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    var gameActivity: GameActivity? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cellSize = width / GRID_WIDTH

        drawGrid(cellSize, canvas)
        drawCurrentTetrimino(cellSize, canvas)
    }

    private fun drawCurrentTetrimino(cellSize: Int, canvas: Canvas) {
        val tetrimino = gameActivity?.currentTetrimino ?: return
        val tetriminoX = gameActivity?.currentTetriminoX ?: 0
        val tetriminoY = (gameActivity?.currentTetriminoY ?: 0)

        paint.color = tetrimino.colorInt
        for (i in tetrimino.shape.indices) {
            for (j in tetrimino.shape.indices) {
                if (tetrimino.shape[i][j] != 0 && tetriminoY + i >= 0) {
                    val left = (tetriminoX + j) * cellSize
                    val top = (tetriminoY + i) * cellSize
                    val right = left + cellSize
                    val bottom = top + cellSize
                    canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                    canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        borderPaint
                    )
                }
            }
        }

    }

    private fun drawGrid(cellSize: Int, canvas: Canvas) {
        val grid = gameActivity?.grid ?: return
        for (i in 0 until GRID_HEIGHT) {
            for (j in 0 until GRID_WIDTH) {
                val left = j * cellSize
                val top = i * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                paint.color = if (grid[i][j] == 0) Color.DKGRAY else Color.LTGRAY
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    paint
                )
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    borderPaint
                )
            }
        }
    }
}
