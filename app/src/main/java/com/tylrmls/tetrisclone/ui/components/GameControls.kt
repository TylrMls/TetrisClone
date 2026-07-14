package com.tylrmls.tetrisclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun GameControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false
) {
    if (isLandscape) {
        LandscapeControls(onMoveLeft, onMoveRight, onRotate, onSoftDrop, onHardDrop, onPause, modifier)
    } else {
        PortraitControls(onMoveLeft, onMoveRight, onRotate, onSoftDrop, onHardDrop, onPause, modifier)
    }
}

@Composable
private fun PortraitControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButton(onPause, Icons.Default.Pause, "Pause")
            ControlButton(onRotate, Icons.Default.Refresh, "Rotate")
            ControlButton(onHardDrop, Icons.Default.VerticalAlignBottom, "Hard Drop")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(onMoveLeft, Icons.Default.KeyboardArrowLeft, "Left")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Empty space or extra button if needed
                Spacer(modifier = Modifier.size(64.dp))
                ControlButton(onSoftDrop, Icons.Default.KeyboardArrowDown, "Soft Drop")
            }
            ControlButton(onMoveRight, Icons.Default.KeyboardArrowRight, "Right")
        }
    }
}

@Composable
private fun LandscapeControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlButton(onPause, Icons.Default.Pause, "Pause")
            ControlButton(onRotate, Icons.Default.Refresh, "Rotate")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlButton(onMoveLeft, Icons.Default.KeyboardArrowLeft, "Left")
            ControlButton(onMoveRight, Icons.Default.KeyboardArrowRight, "Right")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlButton(onSoftDrop, Icons.Default.KeyboardArrowDown, "Soft Drop")
            ControlButton(onHardDrop, Icons.Default.VerticalAlignBottom, "Hard Drop")
        }
    }
}

@Composable
private fun ControlButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
