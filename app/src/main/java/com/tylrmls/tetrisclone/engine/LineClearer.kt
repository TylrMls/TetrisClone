package com.tylrmls.tetrisclone.engine

import com.tylrmls.tetrisclone.model.Board

/**
 * Detects and clears full rows from the board.
 */
class LineClearer {
    /**
     * Clears full rows and returns the new board and the number of lines cleared.
     */
    fun clearLines(board: Board): Pair<Board, Int> {
        return board.clearLines()
    }
}
