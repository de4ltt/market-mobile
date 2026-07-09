package ru.kubsu.market.feature.products.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.kubsu.market.core.model.StorageLocation

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
