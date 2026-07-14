package com.tylrmls.tetrisclone.model

/**
 * Represents actions understood by the engine.
 */
sealed interface GameAction {
    data object Tick : GameAction
    data object MoveLeft : GameAction
    data object MoveRight : GameAction
    data object SoftDrop : GameAction
    data object HardDrop : GameAction
    data object RotateClockwise : GameAction
    data object RotateCounterclockwise : GameAction
}
