package com.tylrmls.tetrisclone.model

/**
 * Represents the 10x20 grid of the Tetris board.
 */
data class Board(
    val rows: Int = 20,
    val columns: Int = 10,
    val grid: List<List<Cell>> = List(rows) { List(columns) { Cell() } }
) {
    /**
     * Checks if a cell is occupied or outside the board boundaries.
     */
    fun isOccupied(row: Int, column: Int): Boolean {
        if (row < 0) return false // Allow pieces to be above the board
        if (row >= rows || column !in 0 until columns) return true
        return grid[row][column].isOccupied
    }

    /**
     * Returns a new board with the given tetromino locked into the grid.
     */
    fun lockTetromino(tetromino: Tetromino): Board {
        val mutableGrid = grid.map { it.toMutableList() }
        tetromino.getOccupiedPositions().forEach { pos ->
            if (pos.row in 0 until rows && pos.column in 0 until columns) {
                mutableGrid[pos.row][pos.column] = Cell(isOccupied = true, type = tetromino.type)
            }
        }
        return copy(grid = mutableGrid.map { it.toList() })
    }

    /**
     * Returns a new board with full lines cleared.
     * This logic is also delegated to LineClearer for specific scoring/event handling.
     */
    fun clearLines(): Pair<Board, Int> {
        val nonFullRows = grid.filter { row -> row.any { !it.isOccupied } }
        val linesCleared = rows - nonFullRows.size
        if (linesCleared == 0) return this to 0

        val emptyRows = List(linesCleared) { List(columns) { Cell() } }
        val newGrid = emptyRows + nonFullRows
        return copy(grid = newGrid) to linesCleared
    }
}
