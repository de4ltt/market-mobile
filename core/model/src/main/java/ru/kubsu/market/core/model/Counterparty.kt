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
import ru.kubsu.market.core.ui.theme.Colors

@Serializable
data class Counterparty(
    val counterpartyId: Int? = null,
    val name: String,
    val address: String,
    val contactInfo: String
) : IItemRepresentable(), IDictionaryItem {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$counterpartyId | $name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Контрагент" to name,
            "Адрес" to address,
            "Контакты" to contactInfo,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    override val endpoint: String
        get() = "counterparties"
    override val className: String
        get() = "Контрагенты"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Контрагенты"
    }
}
