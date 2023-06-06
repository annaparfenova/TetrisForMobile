package com.example.tetris

import androidx.annotation.ColorInt

class Tetrimino(
    val shape: Array<IntArray>,
    @ColorInt val colorInt: Int,
) {

    fun rotateRight(): Tetrimino {
        val newShape = Array(shape.size) { IntArray(shape.size) }
        for (i in shape.indices) {
            for (j in shape.indices) {
                newShape[j][shape.size - 1 - i] = shape[i][j]
            }
        }
        return Tetrimino(newShape, colorInt)
    }

    fun rotateLeft(): Tetrimino {
        val newShape = Array(shape.size) { IntArray(shape.size) }
        for (i in shape.indices) {
            for (j in shape.indices) {
                newShape[shape.size - 1 - j][i] = shape[i][j]
            }
        }
        return Tetrimino(newShape, colorInt)
    }
}
