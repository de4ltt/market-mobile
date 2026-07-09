package ru.kubsu.market.feature.dictionaries.domain

import ru.kubsu.market.core.model.IDictionaryItem

interface DictionariesRepository {
    suspend fun getItems(item: IDictionaryItem): List<IDictionaryItem>
}
