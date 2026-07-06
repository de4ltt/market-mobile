package ru.kubsu.market.model

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import ru.kubsu.market.ui.cringe.AppViewModel
import ru.kubsu.market.ui.theme.Colors

@Serializable
data class SupplyContractItem(
    val supplyContractItemId: Int? = null,
    val supplyContractId: Int,
    val productId: Int,
    val quantity: Int = 1,
    val deliveryType: String
) : IItemRepresentable(), IDictionaryItem {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.Companion.basicMarquee(),
            text = "Изделие №$supplyContractItemId",
            fontSize = 20.sp,
            fontWeight = FontWeight.Companion.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Договор Id" to supplyContractId.toString(),
            "Товар Id" to productId.toString(),
            "Количество" to quantity.toString(),
            "Тип доставки" to deliveryType,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    override val endpoint: String
        get() = "supply-contract-items"
    override val className: String
        get() = "Поставленные товары"

    override fun getItems(viewModel: AppViewModel) {
        viewModel.getDictionaryItems(this)
    }

    companion object {
        val className = "Позиции договора"
    }
}