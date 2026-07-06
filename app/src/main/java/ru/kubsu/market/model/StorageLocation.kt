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
import ru.kubsu.market.ui.theme.Colors

@Serializable
data class StorageLocation(
    val storageLocationId: Int? = null,
    val name: String,
    val type: String,
    val address: String
) : IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.Companion.basicMarquee(),
            text = "$storageLocationId | $name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Companion.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "ID" to "$storageLocationId",
            "Название" to name,
            "Тип" to type,
            "Адрес" to address,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    companion object {
        val className = "Места хранения"
    }
}