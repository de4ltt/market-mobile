package ru.kubsu.market.feature.products.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.feature.products.presentation.model.StorageUiState
import ru.kubsu.market.feature.products.presentation.viewmodel.StorageViewModel

@Composable
fun StorageRoute(
    viewModel: StorageViewModel,
    onShelvesRequested: (Int) -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StorageScreen(
        uiState = uiState,
        onShelvesRequested = onShelvesRequested,
        onRetryClick = viewModel::loadStorageLocations,
        addDialog = addDialog,
        modifier = modifier
    )
}

@Composable
fun StorageScreen(
    uiState: StorageUiState,
    onShelvesRequested: (Int) -> Unit,
    onRetryClick: () -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is StorageUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is StorageUiState.Success -> {
            ItemsRepresentationScreen(
                items = uiState.items,
                className = StorageLocation.className,
                container = { item ->
                    ItemRepresentationCardExpanded(item = item.toUiDisplayable(), onDelete = {}) {
                        onShelvesRequested(item.storageLocationId!!)
                    }
                },
                addDialog = addDialog
            )
        }
        is StorageUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.message,
                        color = Colors.WHITE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        text = "Повторить",
                        buttonType = AppButtonType.POSITIVE,
                        onClick = onRetryClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun StorageScreenSuccessPreview() {
    val sampleLocations = listOf(
        StorageLocation(
            storageLocationId = 1,
            name = "Склад №1",
            type = "Основной",
            address = "ул. Московская, 2"
        )
    )
    StorageScreen(
        uiState = StorageUiState.Success(sampleLocations),
        onShelvesRequested = {},
        onRetryClick = {}
    )
}
