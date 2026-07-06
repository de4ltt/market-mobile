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
import ru.kubsu.market.ui.cringe.serializer.BigDecimalSerializer
import ru.kubsu.market.ui.theme.Colors
import java.math.BigDecimal

@Serializable
data class Truck(
    val truckId: Int? = null,
    val licencePlate: String,
    val model: String,
    @Serializable(with = BigDecimalSerializer::class)
    val capacity: BigDecimal,
    val driverId: Int
) : IItemRepresentable(), IDictionaryItem {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$truckId | $licencePlate",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Госномер" to licencePlate,
            "Модель" to model,
            "Грузоподъёмность" to capacity.toString(),
            "Водитель Id" to driverId.toString(),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    override val endpoint: String
        get() = "trucks"
    override val className: String
        get() = "Грузовики"

    override fun getItems(viewModel: AppViewModel) {
        viewModel.getDictionaryItems(this)
    }

    companion object {
        val className = "Грузовики"
    }
}
