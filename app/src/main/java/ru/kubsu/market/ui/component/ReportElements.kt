package ru.kubsu.market.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.kubsu.market.R
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.model.PersonnelReportStatus
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.core.ui.component.AppButton
import java.math.BigDecimal
import kotlin.text.iterator

@Composable
fun ReportRepresentationCard(
    report: PersonnelReport,
    onEdit: (ConfirmReportRequest) -> Unit,
) {

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    if (isDialogOpen)
        ConfirmReportRequestDialog(
            onDismiss = { isDialogOpen = false },
            onConfirm = { onEdit(it) }
        )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
    ) {
        ru.kubsu.market.core.ui.component.FieldsRepresentation(map = report.displayFields)

        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = { isDialogOpen = true },
                    indication = null,
                    interactionSource = null
                ),
            painter = painterResource(R.drawable.pencil),
            tint = Colors.LIGHT_BLUE,
            contentDescription = "delete_icon"
        )
    }
}

@Composable
fun ConfirmReportRequestDialog(
    initial: ConfirmReportRequest? = null,
    onDismiss: () -> Unit,
    onConfirm: (ConfirmReportRequest) -> Unit
) {
    var directorComment by rememberSaveable { mutableStateOf(initial?.directorComment.orEmpty()) }

    // Empty string => null ("Без изменений")
    var manualOvertimeText by rememberSaveable {
        mutableStateOf(initial?.manualOvertime?.stripTrailingZeros()?.toPlainString().orEmpty())
    }
    var manualUnderworkText by rememberSaveable {
        mutableStateOf(initial?.manualUnderwork?.stripTrailingZeros()?.toPlainString().orEmpty())
    }

    val manualOvertime = manualOvertimeText.toBigDecimalOrNullSafe()
    val manualUnderwork = manualUnderworkText.toBigDecimalOrNullSafe()

    val overtimeOk = manualOvertimeText.isBlank() || manualOvertime != null
    val underworkOk = manualUnderworkText.isBlank() || manualUnderwork != null

    val canSave = overtimeOk && underworkOk

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтверждение отчета") },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    onConfirm(
                        ConfirmReportRequest(
                            directorComment = directorComment.trim().ifBlank { null },
                            manualOvertime = if (manualOvertimeText.isBlank()) null else manualOvertime,
                            manualUnderwork = if (manualUnderworkText.isBlank()) null else manualUnderwork
                        )
                    )
                }
            ) { Text("Подтвердить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = directorComment,
                    onValueChange = { directorComment = it },
                    label = { Text("Комментарий директора") },
                    placeholder = { Text("Можно оставить пустым") },
                    minLines = 2
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = manualOvertimeText,
                    onValueChange = { manualOvertimeText = it.normalizeDecimalInput() },
                    label = { Text("Переработка (вручную)") },
                    placeholder = { Text("Без изменений") },
                    isError = !overtimeOk,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        if (manualOvertimeText.isBlank()) Text("Без изменений")
                        else if (!overtimeOk) Text("Введите число (например, 2 или 2.5)")
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = manualUnderworkText,
                    onValueChange = { manualUnderworkText = it.normalizeDecimalInput() },
                    label = { Text("Недоработка (вручную)") },
                    placeholder = { Text("Без изменений") },
                    isError = !underworkOk,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        if (manualUnderworkText.isBlank()) Text("Без изменений")
                        else if (!underworkOk) Text("Введите число (например, 1 или 1.5)")
                    }
                )
            }
        }
    )
}

private fun String.toBigDecimalOrNullSafe(): BigDecimal? =
    try {
        BigDecimal(trim().replace(',', '.'))
    } catch (_: Exception) {
        null
    }

/**
 * Leaves only digits and a single decimal separator; normalizes ',' to '.'.
 * "" stays "" (so it becomes null and shows "Без изменений").
 */
private fun String.normalizeDecimalInput(): String {
    val s = trim()
    if (s.isEmpty()) return ""

    val cleaned = buildString {
        var dotSeen = false
        for (ch in s) {
            when {
                ch.isDigit() -> append(ch)
                (ch == '.' || ch == ',') && !dotSeen -> {
                    append('.')
                    dotSeen = true
                }
            }
        }
    }

    return if (cleaned.startsWith(".")) "0$cleaned" else cleaned
}
