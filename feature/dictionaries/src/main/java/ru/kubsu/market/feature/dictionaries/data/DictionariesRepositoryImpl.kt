package ru.kubsu.market.feature.dictionaries.data

import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.network.api.DictionariesApi
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository
import javax.inject.Inject

class DictionariesRepositoryImpl @Inject constructor(
    private val dictionariesApi: DictionariesApi
) : DictionariesRepository {

    override suspend fun getItems(item: IDictionaryItem): List<IDictionaryItem> {
        return dictionariesApi.getItems(item)
    }
}
