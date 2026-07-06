package ru.kubsu.market.model

import ru.kubsu.market.ui.cringe.AppViewModel

interface IDictionaryItem {
    val endpoint: String
    val className: String
    fun getItems(viewModel: AppViewModel)
}