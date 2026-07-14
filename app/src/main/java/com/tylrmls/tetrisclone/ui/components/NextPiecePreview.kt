package com.tylrmls.tetrisclone.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tylrmls.tetrisclone.model.Tetromino
import com.tylrmls.tetrisclone.model.TetrominoType
import com.tylrmls.tetrisclone.ui.theme.getTetrominoColor

@Composable
fun NextPiecePreview(
    nextPiece: TetrominoType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "NEXT", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(1.dp, Color.Gray)
                .background(Color.Black)
                .padding(4.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val blockSize = size.width / 4f
                
                nextPiece.relativePositions.forEach { pos ->
                    // Center the piece roughly in the 4x4 preview
                    val drawRow = pos.row + 1
                    val drawCol = pos.column + 1
                    
                    drawRect(
                        color = getTetrominoColor(nextPiece),
                        topLeft = Offset(drawCol * blockSize, drawRow * blockSize),
                        size = Size(blockSize, blockSize)
                    )
                    drawRect(
                        color = Color.Black.copy(alpha = 0.2f),
                        topLeft = Offset(drawCol * blockSize, drawRow * blockSize),
                        size = Size(blockSize, blockSize),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                    )
                }
            }
        }
    }
}
