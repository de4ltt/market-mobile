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
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.feature.products.presentation.model.ShelfProductsUiState
import ru.kubsu.market.feature.products.presentation.viewmodel.ShelfProductsViewModel

@Composable
fun ShelfProductsRoute(
    viewModel: ShelfProductsViewModel,
    shelfId: Int,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShelfProductsScreen(
        uiState = uiState,
        onRetryClick = { viewModel.loadProductsForShelf(shelfId) },
        modifier = modifier
    )
}

@Composable
fun ShelfProductsScreen(
    uiState: ShelfProductsUiState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ShelfProductsUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ShelfProductsUiState.Success -> {
            ItemsRepresentationScreen(
                items = uiState.items.map { it.toUiDisplayable() },
                className = Product.className
            )
        }
        is ShelfProductsUiState.Error -> {
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
private fun ShelfProductsScreenSuccessPreview() {
    val sampleProducts = listOf(
        Product(
            productId = 1,
            name = "Молоко 3.2%",
            manufacturerName = "Кубанская марка",
            manufacturerCountry = "Россия",
            manufacturerCode = "KM-12",
            size = "1",
            unit = "л",
            barcode = "4601234567890",
            additionalInfo = "Пастеризованное",
            storageRequirement = "chilled"
        )
    )
    ShelfProductsScreen(
        uiState = ShelfProductsUiState.Success(sampleProducts),
        onRetryClick = {}
    )
}
