package com.tylrmls.tetrisclone.engine

import com.tylrmls.tetrisclone.model.Board
import com.tylrmls.tetrisclone.model.Position
import com.tylrmls.tetrisclone.model.Tetromino

/**
 * Logic to determine if a tetromino can occupy a requested position/rotation.
 */
class CollisionDetector {
    /**
     * Checks if the tetromino can be placed at the given position and rotation.
     */
    fun canMove(board: Board, tetromino: Tetromino, nextPosition: Position, nextRotation: Int): Boolean {
        val nextPiece = tetromino.copy(position = nextPosition, rotation = nextRotation)
        return nextPiece.getOccupiedPositions().all { pos ->
            !board.isOccupied(pos.row, pos.column)
        }
    }
}
