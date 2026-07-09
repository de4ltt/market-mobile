package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class SupplyContractItem(
    val supplyContractItemId: Int? = null,
    val supplyContractId: Int,
    val productId: Int,
    val quantity: Int = 1,
    val deliveryType: String
) : IDictionaryItem {

    override val endpoint: String
        get() = "supply-contract-items"
    override val className: String
        get() = "Поставленные товары"
    override val serializer: KSerializer<out IDictionaryItem>
        get() = serializer()

    companion object {
        val className = "Позиции договора"
    }
}
