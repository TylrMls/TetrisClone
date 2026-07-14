package com.tylrmls.tetrisclone.engine

import com.tylrmls.tetrisclone.model.*

/**
 * The main logic that takes a State and Action and returns a new State.
 */
class GameEngine(
    private val generator: TetrominoGenerator = TetrominoGenerator(),
    private val collisionDetector: CollisionDetector = CollisionDetector(),
    private val lineClearer: LineClearer = LineClearer(),
    private val rotationSystem: RotationSystem = RotationSystem()
) {
    /**
     * Initializes a new game state.
     */
    fun createGame(): GameState {
        val activePiece = generator.createTetromino(generator.nextType())
        val nextPieceType = generator.nextType()
        val board = Board()
        return GameState(
            board = board,
            activePiece = activePiece,
            ghostPiece = calculateGhostPiece(board, activePiece),
            nextPiece = nextPieceType,
            status = GameStatus.READY
        )
    }

    /**
     * Processes a game action and returns the resulting state.
     */
    fun update(state: GameState, action: GameAction): GameState {
        if (state.status != GameStatus.RUNNING) return state

        val newState = when (action) {
            is GameAction.Tick -> handleTick(state)
            is GameAction.MoveLeft -> handleMove(state, 0, -1)
            is GameAction.MoveRight -> handleMove(state, 0, 1)
            is GameAction.SoftDrop -> handleMove(state, 1, 0)
            is GameAction.HardDrop -> handleHardDrop(state)
            is GameAction.RotateClockwise -> handleRotate(state, clockwise = true)
            is GameAction.RotateCounterclockwise -> handleRotate(state, clockwise = false)
        }
        
        return if (newState.activePiece != state.activePiece || newState.board != state.board) {
            newState.copy(ghostPiece = calculateGhostPiece(newState.board, newState.activePiece))
        } else {
            newState
        }
    }

    private fun calculateGhostPiece(board: Board, piece: Tetromino): Tetromino {
        var ghostPos = piece.position
        while (collisionDetector.canMove(
                board, piece,
                Position(ghostPos.row + 1, ghostPos.column),
                piece.rotation
            )
        ) {
            ghostPos = Position(ghostPos.row + 1, ghostPos.column)
        }
        return piece.copy(position = ghostPos)
    }

    private fun handleTick(state: GameState): GameState {
        val nextPos = Position(state.activePiece.position.row + 1, state.activePiece.position.column)
        return if (collisionDetector.canMove(state.board, state.activePiece, nextPos, state.activePiece.rotation)) {
            state.copy(activePiece = state.activePiece.copy(position = nextPos))
        } else {
            lockPiece(state)
        }
    }

    private fun handleMove(state: GameState, rowOffset: Int, colOffset: Int): GameState {
        val nextPos = Position(state.activePiece.position.row + rowOffset, state.activePiece.position.column + colOffset)
        return if (collisionDetector.canMove(state.board, state.activePiece, nextPos, state.activePiece.rotation)) {
            state.copy(activePiece = state.activePiece.copy(position = nextPos))
        } else {
            state
        }
    }

    private fun handleHardDrop(state: GameState): GameState {
        var currentPiece = state.activePiece
        while (collisionDetector.canMove(
                state.board, currentPiece,
                Position(currentPiece.position.row + 1, currentPiece.position.column),
                currentPiece.rotation
            )
        ) {
            currentPiece = currentPiece.copy(position = Position(currentPiece.position.row + 1, currentPiece.position.column))
        }
        return lockPiece(state.copy(activePiece = currentPiece))
    }

    private fun handleRotate(state: GameState, clockwise: Boolean): GameState {
        val rotatedPiece = if (clockwise) {
            rotationSystem.rotateClockwise(state.activePiece)
        } else {
            rotationSystem.rotateCounterclockwise(state.activePiece)
        }
        return if (collisionDetector.canMove(state.board, state.activePiece, rotatedPiece.position, rotatedPiece.rotation)) {
            state.copy(activePiece = rotatedPiece)
        } else {
            state
        }
    }

    private fun lockPiece(state: GameState): GameState {
        val newBoard = state.board.lockTetromino(state.activePiece)
        val (clearedBoard, linesCleared) = lineClearer.clearLines(newBoard)
        
        val newScore = state.score + calculateScore(linesCleared, state.level)
        val totalLines = state.lines + linesCleared
        val newLevel = (totalLines / 10) + 1
        
        val nextActivePiece = generator.createTetromino(state.nextPiece)
        val nextPieceType = generator.nextType()
        
        // Check for game over
        val isGameOver = !collisionDetector.canMove(
            clearedBoard, nextActivePiece, nextActivePiece.position, nextActivePiece.rotation
        )
        
        return state.copy(
            board = clearedBoard,
            activePiece = nextActivePiece,
            nextPiece = nextPieceType,
            score = newScore,
            lines = totalLines,
            level = newLevel,
            status = if (isGameOver) GameStatus.GAME_OVER else GameStatus.RUNNING
        )
    }

    private fun calculateScore(lines: Int, level: Int): Int {
        val basePoints = when (lines) {
            1 -> 100
            2 -> 300
            3 -> 500
            4 -> 800
            else -> 0
        }
        return basePoints * level
    }
}
