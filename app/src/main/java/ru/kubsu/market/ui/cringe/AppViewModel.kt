package ru.kubsu.market.ui.cringe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.kubsu.market.core.model.*
import ru.kubsu.market.core.network.UserPreferencesRepository
import ru.kubsu.market.core.model.pricing.PriceFormationResult
import ru.kubsu.market.core.model.MenuCategory
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userPrefs: UserPreferencesRepository,
    private val authRepository: ru.kubsu.market.core.network.AuthRepository,
    val httpClient: HttpClient
) : ViewModel() {

    private val stateStack: ArrayDeque<ScreenState> = ArrayDeque()

    val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    fun onEvent(event: ScreenEvent) = when (event) {
        is ScreenEvent.OnMenuCategorySelected -> onMenuCategorySelected(menuCategory = event.menuCategory)
        ScreenEvent.OnBack -> onBack()


        is ScreenEvent.OnShelvesForStorageLocationRequested -> {
            stateStack.add(_state.value)
            _state.value = ScreenState.Shelves(event.storageLocationId)
        }
        is ScreenEvent.OnProductsForShelfRequested -> {
            stateStack.add(_state.value)
            _state.value = ScreenState.ShelfProducts(event.shelfId)
        }
        ScreenEvent.OnReportsConfirm -> onReportsConfirm()
        ScreenEvent.OnReportsRequested -> onReportsRequested()
        is ScreenEvent.OnReportsRequestedForEmployee -> onEmployeeReportsRequested(event.employeeId)
        is ScreenEvent.OnUpdateReport -> onUpdateReport(
            reportId = event.reportId,
            request = event.request,
            reports = event.reports
        )

        ScreenEvent.OnCheckIn -> onCheckIn()
        ScreenEvent.OnCheckOut -> onCheckOut()
        ScreenEvent.OnZeroOvertimeCheckOut -> onZeroOvertimeCheckOut()
        ScreenEvent.OnLogOut -> onLogOut()
    }
    private val _role: MutableStateFlow<Role?> = MutableStateFlow(null)

    val role = _role.asStateFlow()

    var id: Int? = null
    var accessToken: String? = null

    var refreshToken: String? = null
    var vacation: Vacation? = null
        private set

    var vacationDays: Long? = null
        private set
    private val _isCheckedIn = MutableStateFlow(false)

    val isCheckedIn = _isCheckedIn.asStateFlow()

    private fun onLogOut() = proceedInCoroutine {
        clearStack()
        _state.value = ScreenState.Authorization
        authRepository.logout()
        refreshToken = null
        accessToken = null
        id = null
        _role.value = null
        _isCheckedIn.value = false
        vacation = null
        vacationDays = null
    }

    private fun onCheckIn() = proceedInCoroutine(withLoading = false) {
        val result = httpClient.post("$BASE_URL/time-tracking/$id/check-in").body<WorkDay>()
        _isCheckedIn.value = true
        _errorEvents.value = "Начало работы в ${result.checkIn.toString().replace('T', ' ')}"
    }

    private fun onCheckOut() = proceedInCoroutine(withLoading = false) {
        val result = httpClient.post("$BASE_URL/time-tracking/$id/check-out").body<WorkDay>()
        _isCheckedIn.value = false
        _errorEvents.value = "Конец работы в ${result.checkOut.toString().replace('T', ' ')}"
    }

    private fun onZeroOvertimeCheckOut() = proceedInCoroutine(withLoading = false) {
        val result = httpClient.post("$BASE_URL/time-tracking/$id/zero-overtime").body<WorkDay>()
        _isCheckedIn.value = false
        _errorEvents.value = "Конец работы в ${result.checkOut.toString().replace('T', ' ')}"
    }

    private fun onUpdateReport(
        reports: List<PersonnelReport>,
        reportId: Int,
        request: ConfirmReportRequest
    ) = proceedInCoroutine {
        val directorId = requireNotNull(id) { "Director id is required" }

        val updated: PersonnelReport = httpClient.post("$BASE_URL/reports/$reportId/update") {
            url { parameters.append("directorId", directorId.toString()) }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        val mutableReports = reports.toMutableList()
        mutableReports[reports.indexOfFirst { it.personnelReportId == reportId }] = updated
        _state.value = ScreenState.Reports(reports = mutableReports)
    }

    private fun onReportsConfirm() = proceedInCoroutine {
        val directorId = requireNotNull(id) { "Director id is required" }

        httpClient.post("$BASE_URL/reports/confirm-current-week") {
            url { parameters.append("directorId", directorId.toString()) }
        }.body<String>()

        onReportsRequested()
    }

    private fun onEmployeeReportsRequested(employeeId: Int) = proceedInCoroutine {
        val reports: List<PersonnelReport> =
            httpClient.get("$BASE_URL/reports/employee/$employeeId").body()

        _state.value = ScreenState.Reports(reports = reports)
    }

    private fun onReportsRequested() = proceedInCoroutine {
        val reports: List<PersonnelReport> =
            httpClient.get("$BASE_URL/reports/current-week").body()

        _state.value = ScreenState.Reports(reports = reports)
    }








    private suspend fun getWorkDaysForEmployee(employeeId: Int): List<WorkDay> {
        val workDays = httpClient.get("$BASE_URL/time-tracking/$employeeId/work-days") {
            parameter(
                "startDate",
                "${LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong())}"
            )
            parameter("endDate", "${LocalDate.now()}")
        }.body<List<WorkDay>>()
        return workDays
    }

    private fun onMenuCategorySelected(menuCategory: MenuCategory) {
        when (menuCategory) {
            MenuCategory.PRODUCTS -> {
                stateStack.add(_state.value)
                _state.value = ScreenState.Products
            }
            MenuCategory.RECEIVAL -> getReceival()
            MenuCategory.STORAGE -> {
                stateStack.add(_state.value)
                _state.value = ScreenState.Storage
            }
            MenuCategory.EMPLOYEES -> getEmployees()
            MenuCategory.MY_SHIFT -> getMyShift()
            MenuCategory.REPORT -> onReportsRequested()
            MenuCategory.DICTIONARIES -> {
                _state.value = ScreenState.Dictionaries
            }
        }
    }



    private fun getEmployees() {
        stateStack.add(_state.value)
        _state.value = ScreenState.Employees
    }



    private fun getReceival() {
        stateStack.add(_state.value)
        _state.value = ScreenState.ResolveProducts
    }



    private fun getMyShift() = proceedInCoroutine {
        if (id == null)
            getMe()
        val me = httpClient.get("$BASE_URL/employees/$id").body<Employee>()
        val workDays = getWorkDaysForEmployee(me.employeeId!!)
        _role.value = Role.findByRole(me.role) ?: Role.FIRED
        _state.value = ScreenState.Me(
            me = me,
            hours = workDays.sumBy { it.hoursWorked!!.toInt() },
            overwork = workDays.sumBy { it.overtime!!.toInt() },
            underwork = workDays.sumBy { it.underwork!!.toInt() },
            vacation = this.vacation,
            role = _role.value!!,
            daysAvailable = vacationDays ?: 28
        )
    }

    private fun onBack() {
        if (state.value is ScreenState.Authorization)
            return

        if (state.value is ScreenState.MainMenu) {
            getMe()
        }


        if (state.value is ScreenState.Employees) {
            _state.value = ScreenState.MainMenu
            return
        }

        if (_state.value !is ScreenState.Authorization)
            _state.value = stateStack.removeFirstOrNull()
                ?: ScreenState.MainMenu
    }

    fun proceedInCoroutine(withLoading: Boolean = true, operation: suspend () -> Unit) =
        viewModelScope.launch(coroutineExceptionHandler) {
            stateStack.add(_state.value)
            if (withLoading)
                _state.value = ScreenState.Loading
            operation()
        }

    private val _errorEvents = MutableStateFlow<String?>(null)
    val errorEvents = _errorEvents.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorEvents.value = throwable.message ?: "Неизвестная ошибка"
        Log.e("NETWORK ERROR", throwable.message, throwable)
        onBack()
    }

    fun clearErrorEvents() {
        _errorEvents.value = null
    }



    fun refresh() = proceedInCoroutine(withLoading = false) {
        val oldRefresh = refreshToken
        try {
            if (oldRefresh == null) {
                _state.value = ScreenState.Authorization
                throw Exception("No refresh token")
            }
            val response = authRepository.refresh(oldRefresh)
            accessToken = response.access_token
            refreshToken = response.refresh_token
            getMe()
            BearerTokens(response.access_token, response.refresh_token)
        } catch (e: Exception) {
            authRepository.logout()
            accessToken = null
            refreshToken = null
            _state.value = ScreenState.Authorization
            null
        }
    }

    private fun getMe() {
        getMeJob?.cancel()
        getMeJob = viewModelScope.launch {
            _state.value = ScreenState.Loading
            try {
                val response = authRepository.getMe()
                id = response.id
                this@AppViewModel.vacation =
                    httpClient.get("$BASE_URL/vacations/employee/$id").body<List<Vacation>>()
                        .lastOrNull()
                this@AppViewModel.vacationDays =
                    28 - httpClient.get("$BASE_URL/vacations/employee/$id/2026/available")
                        .body<Long>()
                _isCheckedIn.value =
                    httpClient.get("$BASE_URL/time-tracking/$id/is-checked-in").body<Boolean>()
                _role.value = Role.findByRole(response.role) ?: throw Exception()
            } catch (e: Exception) {
                Log.d("EROROR", "${e.message}", e)
                _state.value = ScreenState.Authorization
            }
            _state.value = ScreenState.MainMenu
        }
    }

    private var getMeJob: Job? = null

    init {
        viewModelScope.launch {
            userPrefs.accessToken.collectLatest { token ->
                accessToken = token
                if (token == null) {
                    _state.value = ScreenState.Authorization
                } else {
                    getMe()
                }
            }
        }
    }

    private val _show = MutableStateFlow(false)
    val show = _show.asStateFlow()

    fun startMidnightSchedule(zoneId: ZoneId = ZoneId.systemDefault()) {
        viewModelScope.launch {
            while (true) {
                val delayMs = millisUntilNextMidnight(zoneId)
                kotlinx.coroutines.delay(delayMs)
                _show.value = true

                // If you want: ensure it doesn't immediately re-open if left open:
                // Wait at least 1 second after midnight before scheduling next.
                kotlinx.coroutines.delay(1_000)
            }
        }
    }

    fun dismiss() {
        _show.value = false
    }

    private fun millisUntilNextMidnight(zoneId: ZoneId): Long {
        val now = ZonedDateTime.now(zoneId)
        val nextMidnight = now
            .truncatedTo(ChronoUnit.DAYS)
            .plusDays(1)
        return Duration.between(now, nextMidnight).toMillis().coerceAtLeast(0)
    }

    fun clearStack() {
        stateStack.clear()
    }

}

@Serializable
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)

@Serializable
data class RefreshRequest(
    val refresh_token: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val login: String,
    val role: String,
    val full_name: String?
)

//private const val BASE_URL = "http://10.0.2.2:8081"
const val BASE_URL = "http://10.114.84.195:8081"
private const val BASE_URL_GATE = "http://10.114.84.195:8000"
//private const val BASE_URL_GATE = "http://cp.ae-health.ru"