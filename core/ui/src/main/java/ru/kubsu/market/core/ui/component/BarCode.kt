package ru.kubsu.market.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun BarCode(
    code: String,
    modifier: Modifier = Modifier
) {
    val url = remember(code) {
        "https://quickchart.io/barcode?type=upca&text=$code"
    }

    Box(
        modifier = modifier
            .background(Colors.WHITE, RoundedCornerShape(12.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Barcode",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 3.dp,
                        color = Colors.DARK_BLUE
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Не удалось загрузить штрихкод",
                        color = Colors.DARK_GRAY
                    )
                }
            }
        )
    }
}
