package com.tylrmls.tetrisclone

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkOrientationSupport()
        
        enableEdgeToEdge()
        setContent {
            com.tylrmls.tetrisclone.ui.theme.TetrisCloneTheme {
                com.tylrmls.tetrisclone.ui.GameScreen()
            }
        }
    }

    private fun checkOrientationSupport() {
        val metrics = resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        
        // Smallest dimension is what matters for "height" in landscape
        val smallestDimension = minOf(widthDp, heightDp)
        
        // If the smallest dimension is too small to fit the board vertically (20 rows),
        // lock to portrait. 450dp is a safe threshold (20 rows * 20dp + padding)
        if (smallestDimension < 450) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}
