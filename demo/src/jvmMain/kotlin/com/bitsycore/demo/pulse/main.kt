package com.bitsycore.demo.pulse

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		title = "Pulse Demo",
		state = rememberWindowState(width = 600.dp, height = 960.dp)
	) {
		MaterialTheme {
			AppNavHost()
		}
	}
}
