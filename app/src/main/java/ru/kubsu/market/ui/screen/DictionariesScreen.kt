package ru.kubsu.market.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.R as CoreUiR
import ru.kubsu.market.core.model.ContactPerson
import ru.kubsu.market.core.model.Counterparty
import ru.kubsu.market.core.model.SupplyContract
import ru.kubsu.market.core.model.SupplyContractItem
import ru.kubsu.market.core.model.Truck
import ru.kubsu.market.ui.cringe.AppViewModel
import ru.kubsu.market.core.ui.theme.Colors
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun DictionariesScreen(viewModel: AppViewModel) = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp)
) {
    Text(
        text = "Другие словари",
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
        ).forEach {
            DictionaryItemCard(modifier = Modifier.weight(1f), title = it.className) { it.getItems(viewModel) }
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
            .align(Alignment.CenterEnd)
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