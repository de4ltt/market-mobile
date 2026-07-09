package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Counterparty(
    val counterpartyId: Int? = null,
    val name: String,
    val address: String,
    val contactInfo: String
) : IDictionaryItem {
    override val endpoint: String
        get() = "counterparties"
    override val className: String
        get() = "Контрагенты"
    override val serializer: KSerializer<out IDictionaryItem>
        get() = serializer()

    companion object {
        val className = "Контрагенты"
    }
}
