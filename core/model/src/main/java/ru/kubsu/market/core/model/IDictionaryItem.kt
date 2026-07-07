package ru.kubsu.market.core.model

interface IDictionaryFetcher {
    fun getDictionaryItems(item: IDictionaryItem)
}

interface IDictionaryItem {
    val endpoint: String
    val className: String
    fun getItems(fetcher: IDictionaryFetcher)
}
