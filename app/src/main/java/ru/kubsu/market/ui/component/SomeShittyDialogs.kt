package ru.kubsu.market.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation

@Composable
private fun EditDialogScaffold(
    title: String,
    confirmText: String,
    dismissText: String = "Отмена",
    confirmEnabled: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
        },
        confirmButton = {
            TextButton(
                enabled = confirmEnabled,
                onClick = onConfirm
            ) { Text(confirmText) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(dismissText) } }
    )
}

@Composable
fun StorageLocationEditDialog(
    initial: StorageLocation? = null,
    onDismiss: () -> Unit,
    onConfirm: (StorageLocation) -> Unit
) {
    var name by rememberSaveable(initial?.storageLocationId) { mutableStateOf(initial?.name.orEmpty()) }
    var type by rememberSaveable(initial?.storageLocationId) { mutableStateOf(initial?.type.orEmpty()) }
    var address by rememberSaveable(initial?.storageLocationId) { mutableStateOf(initial?.address.orEmpty()) }

    val isValid = name.isNotBlank() && type.isNotBlank() && address.isNotBlank()

    EditDialogScaffold(
        title = if (initial == null) "Создать место хранения" else "Изменить место хранения",
        confirmText = if (initial == null) "Создать" else "Сохранить",
        confirmEnabled = isValid,
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(
                StorageLocation(
                    storageLocationId = initial?.storageLocationId,
                    name = name.trim(),
                    type = type.trim(),
                    address = address.trim()
                )
            )
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = type,
            onValueChange = { type = it },
            label = { Text("Тип") },
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = { address = it },
            label = { Text("Адрес") }
        )
    }
}

@Composable
fun ShelfEditDialog(
    initial: Shelf? = null,
    storageLocations: List<StorageLocation>,
    onDismiss: () -> Unit,
    onConfirm: (Shelf) -> Unit
) {
    var name by rememberSaveable(initial?.shelfId) { mutableStateOf(initial?.name.orEmpty()) }
    var type by rememberSaveable(initial?.shelfId) { mutableStateOf(initial?.type ?: "Обычный") }
    var selectedLocationId by rememberSaveable(initial?.shelfId) {
        mutableStateOf(initial?.storageLocation?.storageLocationId)
    }

    val selectedLocation = remember(storageLocations, selectedLocationId) {
        storageLocations.firstOrNull { it.storageLocationId == selectedLocationId }
    }

    EditDialogScaffold(
        title = if (initial == null) "Создать полку" else "Изменить полку",
        confirmText = if (initial == null) "Создать" else "Сохранить",
        confirmEnabled = true,
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(
                Shelf(
                    shelfId = initial?.shelfId,
                    name = name.trim().ifBlank { null },
                    storageLocation = selectedLocation,
                    type = type.trim().ifBlank { null }
                )
            )
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = type,
            onValueChange = { type = it },
            label = { Text("Тип") },
            singleLine = true
        )

        StorageLocationDropdown(
            modifier = Modifier.fillMaxWidth(),
            items = storageLocations,
            selected = selectedLocation,
            onSelected = { selectedLocationId = it?.storageLocationId },
            label = "Место хранения (опционально)"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StorageLocationDropdown(
    modifier: Modifier = Modifier,
    items: List<StorageLocation>,
    selected: StorageLocation?,
    onSelected: (StorageLocation?) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selected?.let { "${it.storageLocationId ?: "-"} | ${it.name}" } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("— не выбрано —") },
                onClick = {
                    onSelected(null)
                    expanded = false
                }
            )

            items.forEach { loc ->
                DropdownMenuItem(
                    text = { Text("${loc.storageLocationId ?: "-"} | ${loc.name}") },
                    onClick = {
                        onSelected(loc)
                        expanded = false
                    }
                )
            }
        }
    }
}
