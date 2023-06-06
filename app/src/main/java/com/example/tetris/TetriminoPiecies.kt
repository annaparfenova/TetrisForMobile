package com.example.tetris

import android.graphics.Color

val I = Tetrimino(arrayOf(
    intArrayOf(0, 1, 0, 0),
    intArrayOf(0, 1, 0, 0),
    intArrayOf(0, 1, 0, 0),
    intArrayOf(0, 1, 0, 0)
), colorInt = Color.WHITE)

val O = Tetrimino(arrayOf(
    intArrayOf(1, 1),
    intArrayOf(1, 1)
), colorInt = Color.MAGENTA)

val T = Tetrimino(arrayOf(
    intArrayOf(0, 1, 0),
    intArrayOf(1, 1, 0),
    intArrayOf(0, 1, 0)
), colorInt = Color.BLUE)

val S = Tetrimino(arrayOf(
    intArrayOf(1, 0, 0),
    intArrayOf(1, 1, 0),
    intArrayOf(0, 1, 0)
), colorInt = Color.CYAN)

val Z = Tetrimino(arrayOf(
    intArrayOf(0, 1, 0),
    intArrayOf(1, 1, 0),
    intArrayOf(1, 0, 0)
), colorInt = Color.YELLOW)

val J = Tetrimino(arrayOf(
    intArrayOf(0, 1, 0),
    intArrayOf(0, 1, 0),
    intArrayOf(1, 1, 0)
), colorInt = Color.RED)

val L = Tetrimino(arrayOf(
    intArrayOf(0, 1, 0),
    intArrayOf(0, 1, 0),
    intArrayOf(0, 1, 1)
), colorInt = Color.GREEN)
