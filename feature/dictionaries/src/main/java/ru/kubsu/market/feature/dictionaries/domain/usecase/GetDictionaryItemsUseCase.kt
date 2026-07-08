package ru.kubsu.market.feature.dictionaries.domain.usecase

import javax.inject.Inject
import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository

class GetDictionaryItemsUseCase @Inject constructor(
    private val repository: DictionariesRepository
) {
    suspend operator fun invoke(item: IDictionaryItem): List<IDictionaryItem> {
        return repository.getItems(item)
    }
}
