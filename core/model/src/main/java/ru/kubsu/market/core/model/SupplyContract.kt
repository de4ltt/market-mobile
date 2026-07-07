package ru.kubsu.market.core.model

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
) : IItemRepresentable, IDictionaryItem {

    override val displayName: String
        get() = "${supplyContractId ?: "-"} | $contractorId"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Контрагент Id" to contractorId.toString(),
            "Склад Id" to storageLocationId.toString(),
            "Дата начала" to startDate.toString(),
            "Дата окончания" to endDate.toString(),
        )

    override val endpoint: String
        get() = "supply-contracts"
    override val className: String
        get() = "Договор на поставку"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Договоры поставки"
    }
}
