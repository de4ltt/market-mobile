package ru.kubsu.market.feature.products.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.feature.products.presentation.component.ProductRepresentationCard
import ru.kubsu.market.feature.products.presentation.viewmodel.ProductsUiState
import ru.kubsu.market.feature.products.presentation.viewmodel.ProductsViewModel

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ProductsUiState.Products -> {
            ItemsRepresentationScreen(
                items = state.items,
                className = Product.className,
                container = { item ->
                    ProductRepresentationCard(
                        product = item as Product,
                        priceInfo = state.prices[item.productId]
                    )
                }
            )
        }
        is ProductsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    color = Colors.WHITE,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {}
    }
}

@Composable
fun StorageScreen(
    viewModel: ProductsViewModel,
    onShelvesRequested: (Int) -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ProductsUiState.Storage -> {
            ItemsRepresentationScreen(
                items = state.items,
                className = StorageLocation.className,
                container = { item ->
                    ItemRepresentationCardExpanded(item = item, onDelete = {}) {
                        onShelvesRequested((item as StorageLocation).storageLocationId!!)
                    }
                },
                addDialog = addDialog
            )
        }
        is ProductsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    color = Colors.WHITE,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {}
    }
}

@Composable
fun ShelvesScreen(
    viewModel: ProductsViewModel,
    onProductsRequested: (Int) -> Unit,
    addDialog: @Composable ((onDismiss: () -> Unit) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ProductsUiState.Shelves -> {
            ItemsRepresentationScreen(
                items = state.items,
                className = Shelf.className,
                container = { item ->
                    ItemRepresentationCardExpanded(item = item, onDelete = {}) {
                        onProductsRequested((item as Shelf).shelfId!!)
                    }
                },
                addDialog = addDialog
            )
        }
        is ProductsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    color = Colors.WHITE,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {}
    }
}

@Composable
fun ShelfProductsScreen(
    viewModel: ProductsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.DARK_BLUE)
            }
        }
        is ProductsUiState.ShelfProducts -> {
            ItemsRepresentationScreen(
                items = state.items,
                className = Product.className
            )
        }
        is ProductsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    color = Colors.WHITE,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {}
    }
}
