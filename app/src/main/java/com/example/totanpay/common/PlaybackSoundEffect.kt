package com.example.totanpay.common

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlaybackSoundEffect(
    play: Boolean,
    soundId:Int
) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(play) {
        if (play) {
            mediaPlayer = MediaPlayer.create(
                context,
               soundId
            ).apply {
                setOnCompletionListener {
                    isPlaying = false
                }
                if (!isPlaying) {
                    start()
                    isPlaying = true
                }
            }
        }
    }

    DisposableEffect(play) {
        onDispose {
            if (play) {
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }
}
