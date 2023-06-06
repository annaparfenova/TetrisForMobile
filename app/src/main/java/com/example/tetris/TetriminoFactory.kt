package com.example.tetris


class TetriminoFactory {
    private val tetriminos = listOf(I, O, T, S, Z, J, L)
    private val tetriminoPocket = ArrayDeque<Tetrimino>()

    fun createTetrimino(): Tetrimino {
        if (tetriminoPocket.isEmpty()) {
            tetriminoPocket.addAll(tetriminos.shuffled())
        }
        return tetriminoPocket.removeLast()
    }
}
