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
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.feature.products.presentation.model.ShelvesUiState
import ru.kubsu.market.feature.products.presentation.viewmodel.ShelvesViewModel

@Composable
fun ShelvesRoute(
    viewModel: ShelvesViewModel,
    storageLocationId: Int,
    onProductsRequested: (Int) -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShelvesScreen(
        uiState = uiState,
        onProductsRequested = onProductsRequested,
        onRetryClick = { viewModel.loadShelves(storageLocationId) },
        addDialog = addDialog,
        modifier = modifier
    )
}

@Composable
fun ShelvesScreen(
    uiState: ShelvesUiState,
    onProductsRequested: (Int) -> Unit,
    onRetryClick: () -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ShelvesUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ShelvesUiState.Success -> {
            ItemsRepresentationScreen(
                items = uiState.items,
                className = Shelf.className,
                container = { item ->
                    ItemRepresentationCardExpanded(item = item.toUiDisplayable(), onDelete = {}) {
                        onProductsRequested(item.shelfId!!)
                    }
                },
                addDialog = addDialog
            )
        }
        is ShelvesUiState.Error -> {
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
private fun ShelvesScreenSuccessPreview() {
    val sampleShelves = listOf(
        Shelf(
            shelfId = 1,
            name = "Полка А1",
            type = "Охлаждаемая"
        )
    )
    ShelvesScreen(
        uiState = ShelvesUiState.Success(emptyList(), sampleShelves),
        onProductsRequested = {},
        onRetryClick = {}
    )
}
