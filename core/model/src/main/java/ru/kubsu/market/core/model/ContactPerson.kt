package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ContactPerson(
    val contactPersonId: Int? = null,
    val counterpartyId: Int,
    val phone: String,
    val email: String,
    val fullName: String
) : IDictionaryItem {
    override val endpoint: String
        get() = "contact-persons"
    override val className: String
        get() = "Контактные лица"
    override val serializer: KSerializer<out IDictionaryItem>
        get() = serializer()

    companion object {
        val className = "Контактные лица"
    }
}
