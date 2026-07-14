package com.tylrmls.tetrisclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylrmls.tetrisclone.engine.GameEngine
import com.tylrmls.tetrisclone.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * ViewModel for the Tetris game, managing the game state and the game loop.
 */
class GameViewModel(
    private val gameEngine: GameEngine = GameEngine(),
) : ViewModel() {

    private val _gameState = MutableStateFlow(gameEngine.createGame())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (isActive) {
                val currentStatus = _gameState.value.status
                if (currentStatus == GameStatus.RUNNING) {
                    _gameState.value = gameEngine.update(_gameState.value, GameAction.Tick)
                }
                
                // Tick interval decreases as level increases: 1000ms -> 100ms
                val interval = max(100L, 1000L - ((_gameState.value.level - 1) * 100L))
                delay(interval)
            }
        }
    }

    fun moveLeft() {
        _gameState.value = gameEngine.update(_gameState.value, GameAction.MoveLeft)
    }

    fun moveRight() {
        _gameState.value = gameEngine.update(_gameState.value, GameAction.MoveRight)
    }

    fun rotateClockwise() {
        _gameState.value = gameEngine.update(_gameState.value, GameAction.RotateClockwise)
    }

    fun softDrop() {
        _gameState.value = gameEngine.update(_gameState.value, GameAction.SoftDrop)
    }

    fun hardDrop() {
        _gameState.value = gameEngine.update(_gameState.value, GameAction.HardDrop)
    }

    fun startGame() {
        if (_gameState.value.status == GameStatus.READY || _gameState.value.status == GameStatus.PAUSED) {
            _gameState.value = _gameState.value.copy(status = GameStatus.RUNNING)
        }
    }

    fun pauseGame() {
        if (_gameState.value.status == GameStatus.RUNNING) {
            _gameState.value = _gameState.value.copy(status = GameStatus.PAUSED)
        }
    }

    fun resumeGame() {
        startGame()
    }

    fun restartGame() {
        _gameState.value = gameEngine.createGame()
    }
}
