package com.tylrmls.tetrisclone.engine

import com.tylrmls.tetrisclone.model.Position
import com.tylrmls.tetrisclone.model.Tetromino
import com.tylrmls.tetrisclone.model.TetrominoType

/**
 * Seven-bag randomizer for selecting upcoming pieces.
 */
class TetrominoGenerator {
    private var bag = mutableListOf<TetrominoType>()

    /**
     * Returns the next tetromino type from the shuffled bag.
     */
    fun nextType(): TetrominoType {
        if (bag.isEmpty()) {
            bag.addAll(TetrominoType.entries)
            bag.shuffle()
        }
        return bag.removeAt(0)
    }

    /**
     * Creates a new tetromino at the spawn position.
     */
    fun createTetromino(type: TetrominoType): Tetromino {
        return Tetromino(type, Position(row = 0, column = 4))
    }
}
