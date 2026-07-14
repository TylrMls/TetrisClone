package com.tylrmls.tetrisclone

import com.tylrmls.tetrisclone.engine.GameEngine
import com.tylrmls.tetrisclone.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var gameEngine: GameEngine

    @Before
    fun setUp() {
        gameEngine = GameEngine()
    }

    @Test
    fun testInitialState() {
        val state = gameEngine.createGame()
        assertEquals("Initial status should be READY", GameStatus.READY, state.status)
        assertEquals("Initial score should be 0", 0, state.score)
        assertEquals("Initial level should be 1", 1, state.level)
        assertNotNull("Active piece should not be null", state.activePiece)
        assertNotNull("Next piece should not be null", state.nextPiece)
        assertNotNull("Ghost piece should not be null", state.ghostPiece)
    }

    @Test
    fun testGhostPiece() {
        val state = gameEngine.createGame().copy(status = GameStatus.RUNNING)
        val initialGhostPos = state.ghostPiece?.position
        
        // Move piece left, ghost piece should follow
        val movedState = gameEngine.update(state, GameAction.MoveLeft)
        assertNotEquals("Ghost piece should have updated", initialGhostPos, movedState.ghostPiece?.position)
        assertEquals("Ghost piece should be at the bottom", 18, movedState.ghostPiece?.position?.row ?: 0)
    }

    @Test
    fun testPieceMovement() {
        val initialState = gameEngine.createGame().copy(
            status = GameStatus.RUNNING,
            activePiece = Tetromino(TetrominoType.T, Position(5, 5))
        )

        // Test Move Left
        val leftState = gameEngine.update(initialState, GameAction.MoveLeft)
        assertEquals("Column should decrease by 1", 4, leftState.activePiece.position.column)

        // Test Move Right
        val rightState = gameEngine.update(initialState, GameAction.MoveRight)
        assertEquals("Column should increase by 1", 6, rightState.activePiece.position.column)

        // Test Soft Drop (Move Down)
        val downState = gameEngine.update(initialState, GameAction.SoftDrop)
        assertEquals("Row should increase by 1", 6, downState.activePiece.position.row)
    }

    @Test
    fun testPieceRotation() {
        val initialState = gameEngine.createGame().copy(
            status = GameStatus.RUNNING,
            activePiece = Tetromino(TetrominoType.T, Position(5, 5), rotation = 0)
        )

        // Test Clockwise Rotation
        val cwState = gameEngine.update(initialState, GameAction.RotateClockwise)
        assertEquals("Rotation should be 1", 1, cwState.activePiece.rotation)

        // Test Counter-clockwise Rotation
        val ccwState = gameEngine.update(initialState, GameAction.RotateCounterclockwise)
        assertEquals("Rotation should be 3", 3, ccwState.activePiece.rotation)
    }

    @Test
    fun testLineClearing() {
        // Create a board where the last row is full
        val fullRow = List(10) { Cell(isOccupied = true, type = TetrominoType.I) }
        val grid = List(19) { List(10) { Cell() } } + listOf(fullRow)
        val board = Board(grid = grid)
        
        // Active piece that can be locked immediately
        val activePiece = Tetromino(TetrominoType.O, Position(17, 4))
        val state = GameState(
            board = board,
            activePiece = activePiece,
            nextPiece = TetrominoType.J,
            status = GameStatus.RUNNING
        )
        
        // Hard drop to lock the piece and trigger line clear detection
        val nextState = gameEngine.update(state, GameAction.HardDrop)
        
        assertEquals("One line should be cleared", 1, nextState.lines)
        assertTrue("Score should increase after clearing a line", nextState.score > 0)
        assertFalse("Bottom row should no longer be full", nextState.board.grid[19].all { it.isOccupied })
    }

    @Test
    fun testGameOver() {
        // Create a board where the spawn area is blocked by non-full rows
        // (so they don't get cleared by LineClearer)
        val blockedRow = List(9) { Cell(isOccupied = true) } + listOf(Cell(isOccupied = false))
        val grid = List(2) { blockedRow } + List(18) { List(10) { Cell() } }
        val board = Board(grid = grid)
        
        // Piece far away to avoid immediate collision during its own lock
        val activePiece = Tetromino(TetrominoType.O, Position(18, 4))
        val state = GameState(
            board = board,
            activePiece = activePiece,
            nextPiece = TetrominoType.I,
            status = GameStatus.RUNNING
        )
        
        // Lock the current piece, which triggers spawn of the next piece and collision check
        val nextState = gameEngine.update(state, GameAction.HardDrop)
        
        assertEquals("Game status should be GAME_OVER", GameStatus.GAME_OVER, nextState.status)
    }
}
