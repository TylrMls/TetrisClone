package com.tylrmls.tetrisclone.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tylrmls.tetrisclone.ui.components.*
import com.tylrmls.tetrisclone.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.gameState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                LandscapeLayout(state, viewModel)
            } else {
                PortraitLayout(state, viewModel)
            }

            // Overlay stays on top of everything
            GameOverlay(
                status = state.status,
                score = state.score,
                onStart = viewModel::resumeGame,
                onResume = viewModel::resumeGame,
                onRestart = viewModel::restartGame
            )
        }
    }
}

@Composable
private fun PortraitLayout(
    state: com.tylrmls.tetrisclone.model.GameState,
    viewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Info Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScorePanel(
                score = state.score,
                level = state.level,
                lines = state.lines
            )
            NextPiecePreview(nextPiece = state.nextPiece)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game Board with Overlapping Controls
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            GameBoard(
                board = state.board,
                activePiece = state.activePiece,
                ghostPiece = state.ghostPiece,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(0.5f)
            )

            GameControls(
                onMoveLeft = viewModel::moveLeft,
                onMoveRight = viewModel::moveRight,
                onRotate = viewModel::rotateClockwise,
                onSoftDrop = viewModel::softDrop,
                onHardDrop = viewModel::hardDrop,
                onPause = viewModel::pauseGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                isLandscape = false
            )
        }
    }
}

@Composable
private fun LandscapeLayout(
    state: com.tylrmls.tetrisclone.model.GameState,
    viewModel: GameViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Game Board (Left)
        GameBoard(
            board = state.board,
            activePiece = state.activePiece,
            ghostPiece = state.ghostPiece,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.5f)
        )

        // Side Panel (Right)
        Column(
            modifier = Modifier
                .width(220.dp) // Increased width for better controls
                .fillMaxHeight()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NextPiecePreview(nextPiece = state.nextPiece)
                ScorePanel(
                    score = state.score,
                    level = state.level,
                    lines = state.lines
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            GameControls(
                onMoveLeft = viewModel::moveLeft,
                onMoveRight = viewModel::moveRight,
                onRotate = viewModel::rotateClockwise,
                onSoftDrop = viewModel::softDrop,
                onHardDrop = viewModel::hardDrop,
                onPause = viewModel::pauseGame,
                modifier = Modifier.fillMaxWidth(),
                isLandscape = true
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    com.tylrmls.tetrisclone.ui.theme.TetrisCloneTheme {
        GameScreen()
    }
}
