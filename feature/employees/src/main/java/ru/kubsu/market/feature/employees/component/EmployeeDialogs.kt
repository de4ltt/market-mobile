package ru.kubsu.market.feature.employees.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.VacationType
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

private fun LocalDate.toDisplayString(): String =
    "%02d.%02d.%04d".format(dayOfMonth, monthValue, year)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositionDropdown(
    positions: List<Position>,
    selected: Position?,
    onSelected: (Position) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selected?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Должность") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            positions.forEach { position ->
                DropdownMenuItem(
                    text = { Text(position.name) },
                    onClick = {
                        onSelected(position)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolesDropdown(
    roles: List<Role>,
    selected: Role,
    onSelected: (Role) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Роль") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role.name) },
                    onClick = {
                        onSelected(role)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EmployeeEditDialog(
    initial: Employee? = null,
    positions: List<Position>,
    onDismiss: () -> Unit,
    onConfirm: (Employee) -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf(initial?.fullName.orEmpty()) }
    var passportSeries by rememberSaveable { mutableStateOf(initial?.passportSeries.orEmpty()) }
    var passportNumber by rememberSaveable { mutableStateOf(initial?.passportNumber.orEmpty()) }
    var registrationAddress by rememberSaveable { mutableStateOf(initial?.registrationAddress.orEmpty()) }
    var birthDate by rememberSaveable { mutableStateOf(initial?.birthDate) }

    var selectedPosition by rememberSaveable { mutableStateOf(initial?.position) }
    var department by rememberSaveable { mutableStateOf(initial?.department.orEmpty()) }

    var login by rememberSaveable { mutableStateOf(initial?.login.orEmpty()) }
    var password by rememberSaveable { mutableStateOf(initial?.password.orEmpty()) }
    var role by rememberSaveable { mutableStateOf(Role.SELLER) }

    var workPhone by rememberSaveable { mutableStateOf(initial?.workPhone.orEmpty()) }
    var personalPhone by rememberSaveable { mutableStateOf(initial?.personalPhone.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(initial?.email.orEmpty()) }

    val isValid =
        fullName.isNotBlank() &&
                passportSeries.isNotBlank() &&
                passportNumber.isNotBlank() &&
                registrationAddress.isNotBlank() &&
                birthDate != null &&
                selectedPosition != null &&
                department.isNotBlank() &&
                login.isNotBlank() &&
                password.isNotBlank() &&
                workPhone.isNotBlank() &&
                personalPhone.isNotBlank() &&
                email.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initial == null) "Создать сотрудника" else "Изменить сотрудника")
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onConfirm(
                        Employee(
                            employeeId = initial?.employeeId,
                            fullName = fullName.trim(),
                            passportSeries = passportSeries.trim(),
                            passportNumber = passportNumber.trim(),
                            registrationAddress = registrationAddress.trim(),
                            birthDate = birthDate!!,
                            position = selectedPosition?.copy(positionId = null)!!,
                            department = department.trim(),
                            login = login.trim(),
                            password = password,
                            role = "ROLE_" + role.name,
                            workPhone = workPhone.trim(),
                            personalPhone = personalPhone.trim(),
                            email = email.trim()
                        )
                    )
                }
            ) {
                Text(if (initial == null) "Создать" else "Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("ФИО") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = passportSeries,
                        onValueChange = { passportSeries = it },
                        label = { Text("Серия") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = passportNumber,
                        onValueChange = { passportNumber = it },
                        label = { Text("Номер") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                OutlinedTextField(
                    value = registrationAddress,
                    onValueChange = { registrationAddress = it },
                    label = { Text("Адрес регистрации") },
                    modifier = Modifier.fillMaxWidth()
                )

                LocalDateTextField(
                    label = "Дата рождения",
                    value = birthDate,
                    onDateChanged = { birthDate = it },
                    isError = birthDate == null,
                    supportingText = {
                        if (birthDate == null) Text("Введите дату в формате ДД.ММ.ГГГГ")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                PositionDropdown(
                    positions = positions,
                    selected = selectedPosition,
                    onSelected = { selectedPosition = it }
                )

                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Отдел") },
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Логин") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                RolesDropdown(
                    roles = Role.entries.filter { it != Role.FIRED },
                    selected = role,
                    onSelected = { role = it }
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = workPhone,
                    onValueChange = { workPhone = it },
                    label = { Text("Рабочий телефон") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = personalPhone,
                    onValueChange = { personalPhone = it },
                    label = { Text("Личный телефон") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

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
                .menuAnchor(),
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
