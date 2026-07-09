package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class SupplyContract(
    val supplyContractId: Int? = null,
    val contractorId: Int,
    val storageLocationId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate
) : IDictionaryItem {

    override val endpoint: String
        get() = "supply-contracts"
    override val className: String
        get() = "Договор на поставку"
    override val serializer: KSerializer<out IDictionaryItem>
        get() = serializer()

    companion object {
        val className = "Договоры поставки"
    }
}
