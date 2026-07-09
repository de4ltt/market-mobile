package ru.kubsu.market.feature.dictionaries.presentation.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kubsu.market.core.model.ContactPerson
import ru.kubsu.market.core.model.Counterparty
import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.model.SupplyContract
import ru.kubsu.market.core.model.SupplyContractItem
import ru.kubsu.market.core.model.Truck
import ru.kubsu.market.core.ui.R as CoreUiR
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.core.ui.model.UiDisplayable
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.feature.dictionaries.presentation.model.DictionariesUiEvent
import ru.kubsu.market.feature.dictionaries.presentation.model.DictionariesUiState
import ru.kubsu.market.feature.dictionaries.presentation.viewmodel.DictionariesViewModel
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun DictionariesRoute(
    viewModel: DictionariesViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DictionariesUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DictionariesScreen(
        uiState = uiState,
        onItemClick = viewModel::getDictionaryItems,
        onBackClick = viewModel::reset,
        modifier = modifier
    )
}

@Composable
fun DictionariesScreen(
    uiState: DictionariesUiState,
    onItemClick: (IDictionaryItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is DictionariesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Colors.DARK_BLUE)
                }
            }
            is DictionariesUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Icon(
                            painter = painterResource(CoreUiR.drawable.arrow_left),
                            contentDescription = "Назад",
                            modifier = Modifier
                                .clickable(
                                    onClick = onBackClick,
                                    indication = null,
                                    interactionSource = null
                                )
                                .padding(8.dp),
                            tint = Colors.WHITE
                        )
                        Text(
                            text = state.className,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            color = Colors.WHITE
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        ItemsRepresentationScreen(
                            items = state.items,
                            className = state.className
                        )
                    }
                }
                BackHandler(onBack = onBackClick)
            }
            is DictionariesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = Colors.WHITE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                BackHandler(onBack = onBackClick)
            }
            DictionariesUiState.Idle -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Другие справочники",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black,
                        color = Colors.WHITE
                    )

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            ContactPerson(counterpartyId = 0, phone = "", email = "", fullName = ""),
                            Counterparty(name = "", address = "", contactInfo = ""),
                            SupplyContract(contractorId = -1, storageLocationId = -1, startDate = LocalDate.now(), endDate = LocalDate.now()),
                            SupplyContractItem(supplyContractId = -1, productId = -1, deliveryType = ""),
                            Truck(licencePlate = "", model = "", capacity = BigDecimal.ONE, driverId = -1)
                        ).forEach { item ->
                            DictionaryItemCard(
                                title = item.className,
                                onClick = { onItemClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DictionaryItemCard(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(12.dp))
        .background(Colors.DARK_GRAY)
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(15.dp),
    contentAlignment = Alignment.CenterStart
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Colors.LIGHT_GRAY
    )

    Icon(
        painter = painterResource(CoreUiR.drawable.arrow_left),
        contentDescription = "",
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .rotate(180f)
            .padding(end = 5.dp)
            .fillMaxHeight()
            .scale(1.5f)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        tint = Colors.DARK_BLUE
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun DictionariesScreenIdlePreview() {
    DictionariesScreen(
        uiState = DictionariesUiState.Idle,
        onItemClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun DictionariesScreenSuccessPreview() {
    val mockItems = listOf(
        ContactPerson(contactPersonId = 1, counterpartyId = 1, phone = "+79998887766", email = "test@test.ru", fullName = "Иван Иванов").toUiDisplayable()
    )
    DictionariesScreen(
        uiState = DictionariesUiState.Success(mockItems, "Контактные лица"),
        onItemClick = {},
        onBackClick = {}
    )
}
