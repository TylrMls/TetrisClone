package com.tylrmls.tetrisclone.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScorePanel(
    score: Int,
    level: Int,
    lines: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        StatItem(label = "SCORE", value = score.toString())
        Spacer(modifier = Modifier.height(8.dp))
        StatItem(label = "LEVEL", value = level.toString())
        Spacer(modifier = Modifier.height(8.dp))
        StatItem(label = "LINES", value = lines.toString())
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(text = value, style = MaterialTheme.typography.headlineSmall)
    }
}
