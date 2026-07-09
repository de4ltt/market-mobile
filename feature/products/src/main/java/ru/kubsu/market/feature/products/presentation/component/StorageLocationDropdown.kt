package ru.kubsu.market.feature.products.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.kubsu.market.core.model.StorageLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageLocationDropdown(
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
            modifier = modifier.menuAnchor(androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable),
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
