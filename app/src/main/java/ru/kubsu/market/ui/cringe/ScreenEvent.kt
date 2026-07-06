package ru.kubsu.market.ui.cringe

import ru.kubsu.market.core.model.MenuCategory

sealed interface ScreenEvent {
    data object OnLogOut : ScreenEvent
    data class OnMenuCategorySelected(val menuCategory: MenuCategory) : ScreenEvent
    data class OnShelvesForStorageLocationRequested(val storageLocationId: Int) : ScreenEvent
    data class OnProductsForShelfRequested(val shelfId: Int) : ScreenEvent


    data object OnBack : ScreenEvent

    data object OnCheckIn: ScreenEvent
    data object OnCheckOut: ScreenEvent
    data object OnZeroOvertimeCheckOut: ScreenEvent

}