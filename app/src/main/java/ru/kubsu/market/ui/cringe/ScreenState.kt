package ru.kubsu.market.ui.cringe

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.IItemRepresentable
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.model.Vacation

sealed interface ScreenState {
    data object Loading : ScreenState
    data object Storage : ScreenState
    data object Authorization : ScreenState
    data object Dictionaries : ScreenState
    data object MainMenu : ScreenState
    data class Shelves(val storageLocationId: Int) : ScreenState

    data object Employees : ScreenState

    data class ShelfProducts(val shelfId: Int) : ScreenState
    data object Products : ScreenState
    data class Me(
        val me: Employee,
        val daysAvailable: Long,
        val hours: Int,
        val underwork: Int,
        val overwork: Int,
        val vacation: Vacation?,
        val role: Role
    ) : ScreenState

    data object ResolveProducts : ScreenState

    data class Reports(val employeeId: Int? = null) : ScreenState
}