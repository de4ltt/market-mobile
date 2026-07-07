package ru.kubsu.market.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun LoadingScreen(
    modifier: Modifier
) = Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(
        color = Colors.LIGHT_BLUE
    )
}