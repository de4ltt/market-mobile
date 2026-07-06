package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SupplyContractItem(
    val supplyContractItemId: Int? = null,
    val supplyContractId: Int,
    val productId: Int,
    val quantity: Int = 1,
    val deliveryType: String
) : IItemRepresentable, IDictionaryItem {

    override val displayName: String
        get() = "Изделие №${supplyContractItemId ?: "-"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Договор Id" to supplyContractId.toString(),
            "Товар Id" to productId.toString(),
            "Количество" to quantity.toString(),
            "Тип доставки" to deliveryType,
        )

    override val endpoint: String
        get() = "supply-contract-items"
    override val className: String
        get() = "Поставленные товары"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Позиции договора"
    }
}
