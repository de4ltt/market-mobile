package ru.kubsu.market.ui.cringe

import androidx.compose.ui.input.pointer.PointerId
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.MenuCategory

sealed interface ScreenEvent {
    data object OnFormOrder : ScreenEvent
    data object OnLogOut : ScreenEvent
    data class OnMenuCategorySelected(val menuCategory: MenuCategory) : ScreenEvent
    data class OnAddEmployee(val employee: Employee) : ScreenEvent
    data class OnDeleteEmployee(val employeeId: Int) : ScreenEvent
    data class OnShelvesForStorageLocationRequested(val storageLocationId: Int) : ScreenEvent
    data class OnProductsForShelfRequested(val shelfId: Int) : ScreenEvent
    data class OnVacationResponseGiven(val vacation: Vacation) : ScreenEvent
    data object OnEmployeesRequested : ScreenEvent
    data object OnVacationsRequested : ScreenEvent
    data class OnProductsResolved(
        val acceptedProducts: List<ReceivedProduct>,
        val refusedProducts: List<ReceivedProduct>
    ) : ScreenEvent

    data object OnBack : ScreenEvent

    data object OnCheckIn: ScreenEvent
    data object OnCheckOut: ScreenEvent
    data object OnZeroOvertimeCheckOut: ScreenEvent

    data object OnReportsRequested : ScreenEvent
    data object OnReportsConfirm : ScreenEvent
    data class OnReportsRequestedForEmployee(val employeeId: Int) : ScreenEvent
    data class OnUpdateReport(
        val reports: List<PersonnelReport>,
        val reportId: Int,
        val request: ConfirmReportRequest
    ) : ScreenEvent
}