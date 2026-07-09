package ru.kubsu.market.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.VacationType

@Composable
fun VacationEditDialog(
    initial: Vacation? = null,
    maxDays: Int = 28,
    onDismiss: () -> Unit,
    onConfirm: (Vacation) -> Unit
) {
    val typeOptions = VacationType.entries.map { it.title }
    var type by rememberSaveable { mutableStateOf(initial?.type ?: typeOptions.first()) }
    var startDate by rememberSaveable { mutableStateOf(initial?.startDate) }

    val initialDays = remember(initial) {
        val s = initial?.startDate
        val e = initial?.endDate
        if (s != null && e != null) {
            val diff = java.time.temporal.ChronoUnit.DAYS.between(s, e).toInt() + 1
            diff.coerceIn(1, 28)
        } else 1
    }
    var days by rememberSaveable { mutableStateOf(initialDays) }

    val endDate = remember(startDate, days) {
        startDate?.plusDays((days - 1).toLong())
    }

    val isValid = (type in typeOptions) && (startDate != null) && (days in 1..28)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Создать отпуск" else "Изменить отпуск") },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onConfirm(
                        Vacation(
                            type = VacationType.entries.find { it.title == type }!!.name,
                            startDate = startDate,
                            endDate = endDate
                        )
                    )
                }
            ) { Text(if (initial == null) "Создать" else "Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VacationTypeDropdown(
                    selected = type,
                    items = typeOptions,
                    onSelected = { type = it }
                )

                LocalDateTextField(
                    label = "Дата начала",
                    value = startDate,
                    onDateChanged = { startDate = it },
                    isError = startDate == null,
                    supportingText = {
                        if (startDate == null) Text("Введите дату в формате ДД.ММ.ГГГГ")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DaysSelector(
                    days = days,
                    onDaysChange = { days = it.coerceIn(1, maxDays) }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = endDate?.toDisplayString().orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата окончания") }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacationTypeDropdown(
    selected: String,
    items: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Тип") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DaysSelector(
    days: Int,
    onDaysChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Количество дней: $days")
        Slider(
            value = days.toFloat(),
            onValueChange = { onDaysChange(it.toInt()) },
            valueRange = 1f..28f,
            steps = 26
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                enabled = days > 1,
                onClick = { onDaysChange(days - 1) }
            ) { Text("−") }
            OutlinedButton(
                enabled = days < 28,
                onClick = { onDaysChange(days + 1) }
            ) { Text("+") }
        }
    }
}
