package com.tylrmls.tetrisclone.engine

import com.tylrmls.tetrisclone.model.Tetromino

/**
 * Basic rotation logic (rotate around center).
 */
class RotationSystem {
    /**
     * Returns a new tetromino rotated clockwise.
     */
    fun rotateClockwise(tetromino: Tetromino): Tetromino {
        return tetromino.copy(rotation = (tetromino.rotation + 1) % 4)
    }

    /**
     * Returns a new tetromino rotated counterclockwise.
     */
    fun rotateCounterclockwise(tetromino: Tetromino): Tetromino {
        return tetromino.copy(rotation = (tetromino.rotation + 3) % 4)
    }
}
