package ru.kubsu.market.feature.dictionaries.domain.usecase

import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.model.IItemRepresentable
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository

class GetDictionaryItemsUseCase(
    private val repository: DictionariesRepository
) {
    suspend operator fun invoke(item: IDictionaryItem): List<IItemRepresentable> {
        return repository.getItems(item)
    }
}
