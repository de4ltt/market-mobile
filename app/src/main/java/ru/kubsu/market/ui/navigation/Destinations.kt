package ru.kubsu.market.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable data object Loading : Destination
    @Serializable data object Authorization : Destination
    @Serializable data object MainMenu : Destination
    @Serializable data object Products : Destination
    @Serializable data class ShelfProducts(val shelfId: Int) : Destination
    @Serializable data object Storage : Destination
    @Serializable data class Shelves(val storageLocationId: Int) : Destination
    @Serializable data object Employees : Destination
    @Serializable data object ResolveProducts : Destination
    @Serializable data class Reports(val employeeId: Int? = null) : Destination
    @Serializable data object Dictionaries : Destination
    @Serializable data class Me(val employeeId: Int) : Destination
}
