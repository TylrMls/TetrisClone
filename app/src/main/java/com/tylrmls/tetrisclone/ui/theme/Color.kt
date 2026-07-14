package com.tylrmls.tetrisclone.ui.theme

import androidx.compose.ui.graphics.Color
import com.tylrmls.tetrisclone.model.TetrominoType

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val TetrominoI = Color.Cyan
val TetrominoO = Color.Yellow
val TetrominoT = Color(0xFF800080) // Purple
val TetrominoS = Color.Green
val TetrominoZ = Color.Red
val TetrominoJ = Color.Blue
val TetrominoL = Color(0xFFFFA500) // Orange

fun getTetrominoColor(type: TetrominoType?): Color {
    return when (type) {
        TetrominoType.I -> TetrominoI
        TetrominoType.O -> TetrominoO
        TetrominoType.T -> TetrominoT
        TetrominoType.S -> TetrominoS
        TetrominoType.Z -> TetrominoZ
        TetrominoType.J -> TetrominoJ
        TetrominoType.L -> TetrominoL
        null -> Color.DarkGray
    }
}
