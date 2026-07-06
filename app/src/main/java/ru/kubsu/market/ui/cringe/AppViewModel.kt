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

class AppViewModel(
    private val userPrefs: UserPreferencesRepository
) : ViewModel(), IDictionaryFetcher {

    private val stateStack: ArrayDeque<ScreenState> = ArrayDeque()

    val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    fun onEvent(event: ScreenEvent) = when (event) {
        is ScreenEvent.OnLogin -> onLogin(login = event.login, password = event.password)
        is ScreenEvent.OnMenuCategorySelected -> onMenuCategorySelected(menuCategory = event.menuCategory)
        ScreenEvent.OnBack -> onBack()
        is ScreenEvent.OnProductsResolved -> onResolveProducts(
            toAccept = event.acceptedProducts,
            toRefuse = event.refusedProducts
        )

        is ScreenEvent.OnShelvesForStorageLocationRequested -> getShelvesForStorageLocation(event.storageLocationId)
        is ScreenEvent.OnProductsForShelfRequested -> getProductsForShelf(event.shelfId)
        is ScreenEvent.OnVacationRequested -> onVacationRequested(vacation = event.vacation)
        is ScreenEvent.OnVacationResponseGiven -> onVacationResponseGiven(vacation = event.vacation)
        ScreenEvent.OnEmployeesRequested -> getEmployees()
        ScreenEvent.OnVacationsRequested -> getVacations()
        is ScreenEvent.OnAddEmployee -> onAddEmployee(employee = event.employee)
        is ScreenEvent.OnDeleteEmployee -> onDeleteEmployee(employeeId = event.employeeId)
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
        ScreenEvent.OnFormOrder -> onFormOrder()
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
    private val _orderFormed = MutableStateFlow(false)

    val orderFormed = _orderFormed.asStateFlow()
    private val _isCheckedIn = MutableStateFlow(false)

    val isCheckedIn = _isCheckedIn.asStateFlow()
    private val _pricesByProductId =
        MutableStateFlow<Map<Int, PriceFormationResult.ProductPrice>>(emptyMap())

    val pricesByProductId = _pricesByProductId.asStateFlow()
    private val _productPrices: MutableStateFlow<Map<Int, ProductPrice>> =
        MutableStateFlow(emptyMap())

    val productPrices = _productPrices.asStateFlow()

    private fun onFormOrder() = proceedInCoroutine(withLoading = false) {
        val result: PriceFormationResult =
            httpClient.get("$BASE_URL/pricing/$id/make-order").body()

        _pricesByProductId.value = result.productPrices.associateBy { it.productId }
        _orderFormed.value = true
    }

    override fun getDictionaryItems(item: IDictionaryItem) {
        when (item) {
            is ContactPerson -> getDictionaryItems(item)
            is Counterparty -> getDictionaryItems(item)
            is SupplyContract -> getDictionaryItems(item)
            is SupplyContractItem -> getDictionaryItems(item)
            is Truck -> getDictionaryItems(item)
        }
    }

    inline fun <reified T> getDictionaryItems(item: T)
            where T : IDictionaryItem, T : IItemRepresentable = proceedInCoroutine {
        val items = httpClient
            .get("$BASE_URL/${item.endpoint}")
            .body<List<T>>()

        _state.value = ScreenState.Items(items, item.className)
    }

    private fun onLogOut() = proceedInCoroutine {
        clearStack()
        _state.value = ScreenState.Authorization
        userPrefs.clearTokens()
        refreshToken = null
        accessToken = null
        id = null
        _role.value = null
        _isCheckedIn.value = false
        _pricesByProductId.value = emptyMap()
        _orderFormed.value = false
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

        _state.value = ScreenState.Items(items = reports, className = PersonnelReport.className)
    }

    private fun onReportsRequested() = proceedInCoroutine {
        val reports: List<PersonnelReport> =
            httpClient.get("$BASE_URL/reports/current-week").body()

        _state.value = ScreenState.Reports(reports = reports)
    }


    private fun onAddEmployee(employee: Employee) = proceedInCoroutine {
        httpClient.post("$BASE_URL/employees") {
            contentType(ContentType.Application.Json)
            setBody(employee)
        }.body<Employee>()
        getEmployees()
    }

    private fun onDeleteEmployee(employeeId: Int) = proceedInCoroutine {
        httpClient.post("$BASE_URL/employees/$employeeId/dismiss") {
            parameter("directorId", id)
        }
        getEmployees()
    }

    private fun onVacationResponseGiven(vacation: Vacation) =
        proceedInCoroutine(withLoading = false) {
            httpClient.post("$BASE_URL/vacations/${vacation.vacationId}/${if (vacation.approved) "approve" else "decline"}") {
                contentType(ContentType.Application.Json)
            }.body<Vacation>()
            getVacations()
        }

    private fun getVacations() = proceedInCoroutine(withLoading = false) {
        _state.value = ScreenState.Employees.Loading
        val result = httpClient.get("$BASE_URL/vacations").body<List<Vacation>>()
        _state.value = ScreenState.Employees.Vacations(vacations = result)
    }

    private fun onVacationRequested(vacation: Vacation) = proceedInCoroutine(withLoading = false) {
        this.vacation =
            httpClient.post("$BASE_URL/vacations") {
                contentType(ContentType.Application.Json)
                setBody(vacation.copy(employeeId = id))
            }.body<Vacation>()
    }

    private fun onResolveProducts(
        toAccept: List<ReceivedProduct>,
        toRefuse: List<ReceivedProduct>
    ) = proceedInCoroutine {
        /*httpClient.post("$BASE_URL/received-products/reject") {
            contentType(ContentType.Application.Json)
            setBody(toRefuse)
        }
        httpClient.post("$BASE_URL/received-products/$id/accept") {
            contentType(ContentType.Application.Json)
            setBody(toAccept)
        }*/
        _state.value = ScreenState.MainMenu
    }

    private fun onLogin(login: String, password: String) =
        proceedInCoroutine(withLoading = false) {
            val response = httpClient.submitForm(
                url = "$BASE_URL_GATE/login",
                formParameters = parameters {
                    append("employee_login", login)
                    append("password", password)
                }
            ).body<TokenResponse>()
            userPrefs.saveTokens(response.access_token, response.refresh_token)
            accessToken = response.access_token
            refreshToken = response.refresh_token

            getMe()
            _state.value = ScreenState.MainMenu
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
            MenuCategory.PRODUCTS -> getProducts()
            MenuCategory.RECEIVAL -> getReceival()
            MenuCategory.STORAGE -> getStorage()
            MenuCategory.EMPLOYEES -> getEmployees()
            MenuCategory.MY_SHIFT -> getMyShift()
            MenuCategory.REPORT -> onReportsRequested()
            MenuCategory.DICTIONARIES -> {
                _state.value = ScreenState.Dictionaries
            }
        }
    }

    private fun getProductsForShelf(shelfId: Int) = proceedInCoroutine {
        val products = httpClient.get(
            urlString = "$BASE_URL/shelves/$shelfId/products"
        ).body<List<Product>>()
        _state.value = ScreenState.Items(items = products, className = Product.className)
    }

    private fun getShelvesForStorageLocation(storageLocationId: Int) = proceedInCoroutine {
        val shelves = httpClient.get(
            urlString = "$BASE_URL/storage-locations/$storageLocationId/shelves"
        ).body<List<Shelf>>()
        val storageLocations = httpClient.get(
            urlString = "$BASE_URL/storage-locations"
        ).body<List<StorageLocation>>()
        _state.value = ScreenState.Shelves(storageLocations = storageLocations, items = shelves)
    }

    private fun getEmployees() = proceedInCoroutine(withLoading = false) {
        _state.value = ScreenState.Employees.Loading
        val items = httpClient.get(
            urlString = "$BASE_URL/employees"
        ).body<List<Employee>>()
        val positions = httpClient.get(
            urlString = "$BASE_URL/positions"
        ).body<List<Position>>()
        _state.value = ScreenState.Employees.Employees(employees = items, positions = positions)
    }

    private fun getStorage() = proceedInCoroutine {
        val storageLocations = httpClient.get(
            urlString = "$BASE_URL/storage-locations"
        ).body<List<StorageLocation>>()
        _state.value = ScreenState.Storage(items = storageLocations)
    }

    private fun getReceival() = proceedInCoroutine {
        val products =
            httpClient.get("$BASE_URL/received-products/to-resolve").body<List<ReceivedProduct>>()
        _state.value = ScreenState.ResolveProducts(toResolveProducts = products)
    }

    private fun getProducts() = proceedInCoroutine {
        val items = httpClient.get(
            urlString = "$BASE_URL/products"
        ).body<List<Product>>()
        val prices = httpClient.post("$BASE_URL/pricing/products") {
            contentType(ContentType.Application.Json)
            setBody(items.map { it.productId })
        }.body<List<ProductPrice>>().associateBy { it.productId }
        _state.value = ScreenState.Products(items = items, prices = prices)
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

    val httpClient = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP LOG: $message")
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken!!, refreshToken!!)
                    } else null
                }
                refreshTokens {
                    val oldRefresh = oldTokens?.refreshToken
                    if (oldRefresh == null) {
                        userPrefs.clearTokens()
                        accessToken = null
                        refreshToken = null
                        _state.value = ScreenState.Authorization
                        null
                    } else {
                        try {
                            val response = client.post("$BASE_URL_GATE/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshRequest(oldRefresh))
                            }.body<TokenResponse>()
                            userPrefs.saveTokens(response.access_token, response.refresh_token)
                            accessToken = response.access_token
                            refreshToken = response.refresh_token
                            BearerTokens(response.access_token, response.refresh_token)
                        } catch (e: Exception) {
                            userPrefs.clearTokens()
                            accessToken = null
                            refreshToken = null
                            _state.value = ScreenState.Authorization
                            null
                        }
                    }
                }
            }
        }
    }

    fun refresh() = proceedInCoroutine(withLoading = false) {
        val oldRefresh = refreshToken
        try {
            if (oldRefresh == null) {
                _state.value = ScreenState.Authorization
                throw Exception("No refresh token")
            }
            val response = httpClient.post("$BASE_URL_GATE/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(oldRefresh))
            }.body<TokenResponse>()
            userPrefs.saveTokens(response.access_token, response.refresh_token)
            accessToken = response.access_token
            refreshToken = response.refresh_token
            getMe()
            BearerTokens(response.access_token, response.refresh_token)
        } catch (e: Exception) {
            userPrefs.clearTokens()
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
                val response: UserResponse =
                    httpClient.get("$BASE_URL_GATE/me").body<UserResponse>()
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

class AppViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}