package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactPerson(
    val contactPersonId: Int? = null,
    val counterpartyId: Int,
    val fullName: String,
    val phone: String,
    val email: String
) : IItemRepresentable, IDictionaryItem {

    override val displayName: String
        get() = "${contactPersonId ?: "-"} | $fullName"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ФИО" to fullName,
            "Контрагент Id" to counterpartyId.toString(),
            "Телефон" to phone,
            "Email" to email,
        )

    override val endpoint: String
        get() = "contact-persons"
    override val className: String
        get() = "Контактные лица"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Контактные лица"
    }
}
