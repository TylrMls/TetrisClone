package com.tylrmls.tetrisclone.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.tylrmls.tetrisclone.model.Board
import com.tylrmls.tetrisclone.model.Tetromino
import com.tylrmls.tetrisclone.ui.theme.getTetrominoColor

@Composable
fun GameBoard(
    board: Board,
    activePiece: Tetromino?,
    ghostPiece: Tetromino?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .border(2.dp, Color.Gray)
            .background(Color.Black)
            .padding(1.dp)
    ) {
        val blockSize = constraints.maxWidth / board.columns.toFloat()

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw locked cells
            board.grid.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, cell ->
                    if (cell.isOccupied) {
                        drawBlock(
                            row = rowIndex,
                            col = colIndex,
                            blockSize = blockSize,
                            color = getTetrominoColor(cell.type)
                        )
                    }
                }
            }

            // Draw ghost piece
            ghostPiece?.getOccupiedPositions()?.forEach { pos ->
                if (pos.row >= 0) {
                    drawBlock(
                        row = pos.row,
                        col = pos.column,
                        blockSize = blockSize,
                        color = getTetrominoColor(ghostPiece.type).copy(alpha = 0.3f)
                    )
                }
            }

            // Draw active piece
            activePiece?.getOccupiedPositions()?.forEach { pos ->
                if (pos.row >= 0) {
                    drawBlock(
                        row = pos.row,
                        col = pos.column,
                        blockSize = blockSize,
                        color = getTetrominoColor(activePiece.type)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBlock(
    row: Int,
    col: Int,
    blockSize: Float,
    color: Color
) {
    drawRect(
        color = color,
        topLeft = Offset(col * blockSize, row * blockSize),
        size = Size(blockSize, blockSize)
    )
    drawRect(
        color = Color.Black.copy(alpha = 0.2f),
        topLeft = Offset(col * blockSize, row * blockSize),
        size = Size(blockSize, blockSize),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
    )
}
