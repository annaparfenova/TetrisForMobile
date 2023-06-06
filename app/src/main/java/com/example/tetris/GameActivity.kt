package com.example.tetris

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

class GameActivity : AppCompatActivity() {

    val grid = Array(GRID_HEIGHT) { IntArray(GRID_WIDTH) }
    var currentTetrimino: Tetrimino? = null
        private set
    var currentTetriminoX = 0
        private set
    var currentTetriminoY = 0
        private set

    private var speedUp: AtomicInteger = AtomicInteger(0)

    private val score: AtomicInteger = AtomicInteger(0)
    private val totalLines: AtomicInteger = AtomicInteger(0)
    private val level
        get() = (1 + totalLines.get() / LINES_TO_NEW_LEVEL).coerceAtMost(MAX_LEVEL)

    private var gameView: GameView? = null
    private var scoreText: TextView? = null
    private var levelText: TextView? = null

    private val tetriminoFactory = TetriminoFactory()

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            moveTetriminoDown()
            clearLines()
            if (isGameOver()) {
                // End the game
                restartGame()
            } else {
                // Adjust the delay to control the speed of the game
                val gameSpeed = if (speedUp.get() == 1) {
                    min(SPEED_UP_SPEED, START_DELAY_MS / level)
                } else {
                    START_DELAY_MS / level
                }
                handler.postDelayed(this, gameSpeed)
            }
        }
    }

    private fun restartGame() {
        score.getAndSet(0)
        totalLines.getAndSet(0)
        speedUp.getAndSet(0)
        scoreText?.text = "score: ${score.get()}"
        levelText?.text = "level: $level"
        initializeGrid()
        handler.postDelayed(runnable, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameView = findViewById(R.id.gameView)
        gameView?.gameActivity = this

        scoreText = findViewById(R.id.scoreText)
        levelText = findViewById(R.id.levelText)

        scoreText?.text = "score: ${score.get()}"
        levelText?.text = "level: $level"

        setupControlButtons()

        initializeGrid()
        spawnTetrimino()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupControlButtons() {
        findViewById<AppCompatImageView>(R.id.leftButton).setOnTouchListener(
            RepeatOnTouchListener(ACTION_REPEAT_DELAY) {
                moveTetriminoLeft()
            }
        )
        findViewById<AppCompatImageView>(R.id.rightButton).setOnTouchListener(
            RepeatOnTouchListener(ACTION_REPEAT_DELAY) {
                moveTetriminoRight()
            }
        )
        findViewById<AppCompatImageView>(R.id.upButton).setOnClickListener {
            rotateTetrimino()
        }
        findViewById<AppCompatImageView>(R.id.downButton).setOnTouchListener(
            OnTouchOnDeTouchListener(
                onTouchAction = {
                    speedUp.getAndSet(1)
                    handler.removeCallbacks(runnable)
                    handler.post(runnable)
                },
                onDeTouchAction = {
                    speedUp.getAndSet(0)
                    handler.removeCallbacks(runnable)
                    handler.post(runnable)
                }
            )
        )
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun initializeGrid() {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                grid[i][j] = 0
            }
        }
    }

    private fun spawnTetrimino() {
        val newTetrimino = tetriminoFactory.createTetrimino()
        currentTetrimino = newTetrimino
        currentTetriminoX = GRID_WIDTH / 2 - 1
        currentTetriminoY = -newTetrimino.shape.size
    }

    fun moveTetriminoDown() {
        if (canMoveTo(currentTetrimino, currentTetriminoX, currentTetriminoY + 1)) {
            // If the Tetrimino can move down, move it down
            currentTetriminoY++
        } else {
            // If the Tetrimino can't move down, it has landed
            mergeTetrimino()
            clearLines()
            spawnTetrimino()
        }
        gameView?.invalidate()
    }

    private fun canMoveTo(tetrimino: Tetrimino?, newX: Int, newY: Int): Boolean {
        if (tetrimino == null) {
            return false
        }

        for (i in tetrimino.shape.indices) {
            for (j in tetrimino.shape.indices) {
                if (tetrimino.shape[i][j] != 0) {
                    val x = newX + j
                    val y = newY + i

                    // Check if the new position is outside the grid
                    if (x < 0 || x >= GRID_WIDTH || y >= GRID_HEIGHT) {
                        return false
                    }

                    // Check if the new position is occupied
                    if (y >= 0 && grid[y][x] != 0) {
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun moveTetriminoLeft() {
        if (canMoveTo(currentTetrimino, currentTetriminoX - 1, currentTetriminoY)) {
            currentTetriminoX--
            gameView?.invalidate()
        }
    }

    private fun moveTetriminoRight() {
        if (canMoveTo(currentTetrimino, currentTetriminoX + 1, currentTetriminoY)) {
            currentTetriminoX++
            gameView?.invalidate()
        }
    }

    private fun rotateTetrimino() {
        val tetrimino = currentTetrimino ?: return
        val rotatedTetrimino = tetrimino.rotateRight()
        if (canMoveTo(rotatedTetrimino, currentTetriminoX, currentTetriminoY)) {
            currentTetrimino = rotatedTetrimino
            gameView?.invalidate()
        }
    }

    private fun mergeTetrimino() {
        val tetrimino = currentTetrimino ?: return
        for (i in tetrimino.shape.indices) {
            for (j in tetrimino.shape.indices) {
                if (tetrimino.shape[i][j] != 0 && currentTetriminoY + i >= 0) {
                    grid[currentTetriminoY + i][currentTetriminoX + j] = 1
                }
            }
        }
    }

    fun clearLines() {
        var linesCleared = 0
        for (i in 0 until GRID_HEIGHT) {
            if (grid[i].all { it != 0 }) {
                // If the line is full, remove it and move all lines above it down
                for (j in i downTo 1) {
                    grid[j] = grid[j - 1]
                }
                grid[0] = IntArray(GRID_WIDTH)
                linesCleared++
            }
        }
        totalLines.addAndGet(linesCleared)
        when (linesCleared) {
            1 -> addScore(SCORE_ONE_LINE * level)
            2 -> addScore(SCORE_TWO_LINES * level)
            3 -> addScore(SCORE_THREE_LINES * level)
            4 -> addScore(SCORE_FOUR_LINES * level)
        }
    }

    private fun addScore(points: Int) {
        score.addAndGet(points)
        scoreText?.text = "score: ${score.get()}"
        levelText?.text = "level: $level"
    }

    private fun isGameOver(): Boolean {
        return grid[0].any { it != 0 }
    }
}
