package ru.kubsu.market.feature.dictionaries.domain

import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.model.IItemRepresentable

interface DictionariesRepository {
    suspend fun getItems(item: IDictionaryItem): List<IItemRepresentable>
}
