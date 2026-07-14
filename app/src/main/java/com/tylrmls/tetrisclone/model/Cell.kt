package com.tylrmls.tetrisclone.model

/**
 * Represents one board location.
 */
data class Cell(
    val isOccupied: Boolean = false,
    val type: TetrominoType? = null
)
