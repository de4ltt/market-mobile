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
data class ContactPerson(
    val contactPersonId: Int? = null,
    val counterpartyId: Int,
    val fullName: String,
    val phone: String,
    val email: String
) : IItemRepresentable(), IDictionaryItem {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.Companion.basicMarquee(),
            text = "$contactPersonId | $fullName",
            fontSize = 20.sp,
            fontWeight = FontWeight.Companion.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "ФИО" to fullName,
            "Контрагент Id" to counterpartyId.toString(),
            "Телефон" to phone,
            "Email" to email,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    override val endpoint: String
        get() = "contact-persons"
    override val className: String
        get() = "Контактные лица"

    override fun getItems(viewModel: AppViewModel) {
        viewModel.getDictionaryItems(this)
    }

    companion object {
        val className = "Контактные лица"
    }
}