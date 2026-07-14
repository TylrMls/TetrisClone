package com.tylrmls.tetrisclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tylrmls.tetrisclone.model.GameStatus

@Composable
fun GameOverlay(
    status: GameStatus,
    score: Int,
    onStart: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (status == GameStatus.RUNNING) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val title = when (status) {
                GameStatus.READY -> "TETRIS"
                GameStatus.PAUSED -> "PAUSED"
                GameStatus.GAME_OVER -> "GAME OVER"
                else -> ""
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            if (status == GameStatus.GAME_OVER) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Final Score: $score",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (status) {
                GameStatus.READY -> {
                    Button(onClick = onStart) {
                        Text("START GAME")
                    }
                }
                GameStatus.PAUSED -> {
                    Button(onClick = onResume) {
                        Text("RESUME")
                    }
                }
                GameStatus.GAME_OVER -> {
                    Button(onClick = onRestart) {
                        Text("RESTART")
                    }
                }
                else -> {}
            }
        }
    }
}
