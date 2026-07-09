package ru.kubsu.market.feature.employees.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.ui.component.LocalDateTextField

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
