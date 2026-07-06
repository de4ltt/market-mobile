package ru.kubsu.market.core.model

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
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import ru.kubsu.market.core.ui.theme.Colors
import java.time.LocalDate

@Serializable
data class SupplyContract(
    val supplyContractId: Int? = null,
    val contractorId: Int,
    val storageLocationId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate
) : IItemRepresentable(), IDictionaryItem {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$supplyContractId | $contractorId",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Контрагент Id" to contractorId.toString(),
            "Склад Id" to storageLocationId.toString(),
            "Дата начала" to startDate.toString(),
            "Дата окончания" to endDate.toString(),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    override val endpoint: String
        get() = "supply-contracts"
    override val className: String
        get() = "Договор на поставку"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Договоры поставки"
    }
}
