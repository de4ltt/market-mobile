package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Counterparty(
    val counterpartyId: Int? = null,
    val name: String,
    val address: String,
    val contactInfo: String
) : IItemRepresentable, IDictionaryItem {

    override val displayName: String
        get() = "${counterpartyId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Контрагент" to name,
            "Адрес" to address,
            "Контакты" to contactInfo,
        )

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
