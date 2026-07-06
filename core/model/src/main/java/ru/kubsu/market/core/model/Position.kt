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
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import ru.kubsu.market.core.ui.theme.Colors
import java.math.BigDecimal

@Serializable
data class Position(
    val positionId: Int? = null,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val salaryRate: BigDecimal
) : IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$positionId | $name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Должность" to name,
            "Описание" to description,
            "Месячные часы" to monthlyHours.toString(),
            "Ставка" to salaryRate.toString(),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    companion object {
        val className = "Должности"
    }
}
