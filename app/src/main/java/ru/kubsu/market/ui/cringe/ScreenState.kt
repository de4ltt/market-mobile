package ru.kubsu.market.ui.cringe

import ru.kubsu.market.model.Employee
import ru.kubsu.market.model.IItemRepresentable
import ru.kubsu.market.model.PersonnelReport
import ru.kubsu.market.model.Position
import ru.kubsu.market.model.Product
import ru.kubsu.market.model.ProductPrice
import ru.kubsu.market.model.ReceivedProduct
import ru.kubsu.market.model.Role
import ru.kubsu.market.model.Shelf
import ru.kubsu.market.model.StorageLocation
import ru.kubsu.market.model.Vacation

sealed interface ScreenState {
    data object Loading : ScreenState
    data class Storage(val items: List<StorageLocation>) : ScreenState
    data object Authorization : ScreenState
    data object Dictionaries : ScreenState
    data object MainMenu : ScreenState
    data class Shelves(val storageLocations: List<StorageLocation>, val items: List<Shelf>) :
        ScreenState

    sealed interface Employees : ScreenState {
        data class Employees(val employees: List<Employee>, val positions: List<Position>) :
            ScreenState.Employees

        data class Vacations(val vacations: List<Vacation>) : ScreenState.Employees
        data object Loading : ScreenState.Employees
    }

    data class Items(val items: List<IItemRepresentable>, val className: String) : ScreenState
    data class Products(val items: List<Product>, val prices: Map<Int, ProductPrice>) : ScreenState
    data class Me(
        val me: Employee,
        val daysAvailable: Long,
        val hours: Int,
        val underwork: Int,
        val overwork: Int,
        val vacation: Vacation?,
        val role: Role
    ) : ScreenState

    data class ResolveProducts(val toResolveProducts: List<ReceivedProduct>) : ScreenState

    data class Reports(val reports: List<PersonnelReport>): ScreenState
//    data class EmployeeReports(val reports: List<PersonnelReport>): ScreenState
}