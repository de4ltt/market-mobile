package ru.kubsu.market.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kubsu.market.ui.theme.Colors

@Composable
fun MidnightQuestionDialog(
    question: String,
    secondsToAnswer: Int = 30,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onTimeout: () -> Unit = onNo,
    onDismiss: () -> Unit = onNo,
) {
    var remaining by remember { mutableIntStateOf(secondsToAnswer) }

    LaunchedEffect(Unit) {
        while (remaining > 0) {
            kotlinx.coroutines.delay(1_000)
            remaining--
        }
        onTimeout()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Colors.DARK_GRAY,
        title = { Text("На часах 00", color = Colors.WHITE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(question, color = Colors.WHITE)
                Text(
                    "Осталось времени: ${remaining / 60} мин. ${remaining % 60} сек.",
                    color = Colors.WHITE
                )
                LinearProgressIndicator(
                    color = Colors.WHITE,
                    progress = { remaining.toFloat() / secondsToAnswer.toFloat() }
                )
            }
        },
        dismissButton = {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = AppButtonType.POSITIVE,
                text = "Продолжить",
                onClick = onNo
            )
        },
        confirmButton = {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = AppButtonType.NEGATIVE,
                text = "Закончить смену",
                onClick = onYes
            )
        }
    )
}
