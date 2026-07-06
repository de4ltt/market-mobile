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
    data class OnShelvesForStorageLocationRequested(val storageLocationId: Int) : ScreenEvent
    data class OnProductsForShelfRequested(val shelfId: Int) : ScreenEvent


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