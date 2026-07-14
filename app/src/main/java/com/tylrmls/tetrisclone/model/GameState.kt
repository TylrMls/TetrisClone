package com.tylrmls.tetrisclone.model

/**
 * An immutable snapshot of the complete game state.
 */
data class GameState(
    val board: Board = Board(),
    val activePiece: Tetromino,
    val ghostPiece: Tetromino? = null,
    val nextPiece: TetrominoType,
    val score: Int = 0,
    val lines: Int = 0,
    val level: Int = 1,
    val status: GameStatus = GameStatus.READY
)
