package ru.kubsu.market.feature.products.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation

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
