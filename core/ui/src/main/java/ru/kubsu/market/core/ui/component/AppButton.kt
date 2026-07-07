package ru.kubsu.market.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.theme.Colors

enum class AppButtonType(
    val buttonColors: List<Color>
) {
    DEFAULT(
        buttonColors = listOf(
            Colors.DARK_GRAY,
            Colors.DARK_GRAY
        )
    ),
    APPROVED(
        buttonColors = listOf(
            Colors.GREEN,
            Colors.YELLOW
        )
    ),
    POSITIVE(
        buttonColors = listOf(
            Colors.DARK_BLUE,
            Colors.LIGHT_BLUE
        )
    ),
    NEGATIVE(
        buttonColors = listOf(
            Colors.RED,
            Colors.ORANGE
        )
    )
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    buttonType: AppButtonType = AppButtonType.POSITIVE,
    onClick: () -> Unit = {}
) {

    val buttonColors = buttonType.buttonColors.mapIndexed { index, color ->
        animateColorAsState(
            targetValue = if (enabled) color else Colors.DARK_GRAY,
            animationSpec = tween(delayMillis = index * 100)
        ).value
    }

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .wrapContentHeight()
            .background(brush = Brush.linearGradient(colors = buttonColors))
            .padding(15.dp)
            .clickable(
                onClick = { if (enabled) onClick() },
                interactionSource = null,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.WHITE
        )
    }
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: AnnotatedString,
    buttonType: AppButtonType = AppButtonType.POSITIVE,
    onClick: () -> Unit = {}
) {

    val buttonColors = buttonType.buttonColors.mapIndexed { index, color ->
        animateColorAsState(
            targetValue = if (enabled) color else Colors.DARK_GRAY,
            animationSpec = tween(delayMillis = index * 100)
        ).value
    }

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .wrapContentHeight()
            .background(brush = Brush.linearGradient(colors = buttonColors))
            .padding(15.dp)
            .clickable(
                onClick = { if (enabled) onClick() },
                interactionSource = null,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.WHITE
        )
    }
}
