package com.tylrmls.tetrisclone.model

/**
 * Represents the currently falling piece.
 */
data class Tetromino(
    val type: TetrominoType,
    val position: Position,
    val rotation: Int = 0
) {
    /**
     * Calculates the absolute positions occupied by this tetromino on the board.
     */
    fun getOccupiedPositions(): List<Position> {
        return type.relativePositions.map { offset ->
            val rotatedOffset = rotate(offset, type, rotation)
            Position(
                position.row + rotatedOffset.row,
                position.column + rotatedOffset.column
            )
        }
    }

    companion object {
        /**
         * Rotates a relative position around (0,0) based on the tetromino type.
         */
        fun rotate(pos: Position, type: TetrominoType, rotation: Int): Position {
            if (type == TetrominoType.O) return pos
            
            var rotated = pos
            val steps = (rotation % 4 + 4) % 4
            repeat(steps) {
                rotated = Position(rotated.column, -rotated.row)
            }
            return rotated
        }
    }
}
