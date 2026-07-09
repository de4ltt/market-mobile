package ru.kubsu.market.core.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDate

@Composable
fun LocalDateTextField(
    label: String,
    value: LocalDate?,
    onDateChanged: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: (@Composable (() -> Unit))? = null
) {
    var tfv by remember {
        mutableStateOf(TextFieldValue(value?.toDisplayString().orEmpty()))
    }

    LaunchedEffect(value) {
        val newText = value?.toDisplayString().orEmpty()
        if (newText != tfv.text) {
            tfv = TextFieldValue(newText, selection = TextRange(newText.length))
        }
    }

    OutlinedTextField(
        modifier = modifier,
        value = tfv,
        onValueChange = { incoming ->
            val digitsBeforeCursor = incoming.text
                .take(incoming.selection.end)
                .count(Char::isDigit)

            val rawDigits = incoming.text.filter(Char::isDigit).take(8)
            val correctedDigits = clampDayMonth(rawDigits)

            val formatted = formatDigitsAsDate(correctedDigits)

            val newCursor = cursorIndexForDigitCount(
                formatted = formatted,
                digitCount = digitsBeforeCursor.coerceIn(0, correctedDigits.length)
            )

            tfv = TextFieldValue(
                text = formatted,
                selection = TextRange(newCursor)
            )

            onDateChanged(parseLocalDate(formatted))
        },
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("ДД.ММ.ГГГГ") }
    )
}

private fun clampDayMonth(digits: String): String {
    val d = digits.take(2)
    val m = digits.drop(2).take(2)
    val y = digits.drop(4)

    val dd = when {
        d.length < 2 -> d
        else -> d.toInt().coerceIn(1, 31).toString().padStart(2, '0')
    }

    val mm = when {
        m.length < 2 -> m
        else -> m.toInt().coerceIn(1, 12).toString().padStart(2, '0')
    }

    return dd + mm + y
}

private fun formatDigitsAsDate(digits: String): String {
    val day = digits.take(2)
    val month = digits.drop(2).take(2)
    val year = digits.drop(4).take(4)

    return buildString {
        append(day)
        if (digits.length >= 3) append(".")
        append(month)
        if (digits.length >= 5) append(".")
        append(year)
    }
}

private fun cursorIndexForDigitCount(formatted: String, digitCount: Int): Int {
    if (digitCount <= 0) return 0
    var seen = 0
    for (i in formatted.indices) {
        if (formatted[i].isDigit()) {
            seen++
            if (seen == digitCount) return i + 1
        }
    }
    return formatted.length
}

private fun parseLocalDate(text: String): LocalDate? {
    if (!Regex("""\d{2}\.\d{2}\.\d{4}""").matches(text)) return null
    val (d, m, y) = text.split(".").map { it.toInt() }

    return try {
        LocalDate.of(y, m, d)
    } catch (_: Exception) {
        null
    }
}

internal fun LocalDate.toDisplayString(): String =
    "%02d.%02d.%04d".format(dayOfMonth, monthValue, year)
