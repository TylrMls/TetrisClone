package com.tylrmls.tetrisclone.model

/**
 * Defines the seven standard tetromino types and their initial shapes.
 * Offsets are relative to a pivot point.
 */
enum class TetrominoType(val relativePositions: List<Position>) {
    I(listOf(Position(0, -1), Position(0, 0), Position(0, 1), Position(0, 2))),
    O(listOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1))),
    T(listOf(Position(0, -1), Position(0, 0), Position(0, 1), Position(1, 0))),
    S(listOf(Position(0, 0), Position(0, 1), Position(1, -1), Position(1, 0))),
    Z(listOf(Position(0, -1), Position(0, 0), Position(1, 0), Position(1, 1))),
    J(listOf(Position(0, -1), Position(0, 0), Position(0, 1), Position(1, 1))),
    L(listOf(Position(0, -1), Position(0, 0), Position(0, 1), Position(1, -1)))
}
