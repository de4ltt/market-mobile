package ru.kubsu.market

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.ui.component.LogoWithRole
import ru.kubsu.market.ui.component.MidnightQuestionDialog
import ru.kubsu.market.ui.component.ProductRepresentationCard
import ru.kubsu.market.ui.component.ReportRepresentationCard
import ru.kubsu.market.ui.component.ShelfEditDialog
import ru.kubsu.market.ui.component.StorageLocationEditDialog
import ru.kubsu.market.ui.cringe.AppViewModel
import ru.kubsu.market.ui.cringe.AppViewModelFactory
import ru.kubsu.market.ui.cringe.ScreenEvent
import ru.kubsu.market.ui.cringe.ScreenEvent.OnBack
import ru.kubsu.market.ui.cringe.ScreenEvent.OnProductsForShelfRequested
import ru.kubsu.market.ui.cringe.ScreenEvent.OnShelvesForStorageLocationRequested
import ru.kubsu.market.ui.cringe.ScreenEvent.OnUpdateReport
import ru.kubsu.market.ui.cringe.ScreenState
import io.ktor.client.HttpClient
import ru.kubsu.market.core.network.UserPreferencesRepository
import ru.kubsu.market.core.network.userDataStore
import ru.kubsu.market.feature.auth.AuthScreen
import ru.kubsu.market.feature.dictionaries.DictionariesScreen
import ru.kubsu.market.feature.employees.EmployeeScreen
import ru.kubsu.market.feature.employees.EmployeesScreenState
import ru.kubsu.market.core.ui.component.ItemRepresentationCard
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.ui.screen.LoadingScreen
import ru.kubsu.market.feature.mainmenu.MainMenuScreen
import ru.kubsu.market.feature.receival.ReceivalScreen
import ru.kubsu.market.feature.shift.ShiftScreen
import ru.kubsu.market.core.ui.theme.Colors
import java.time.ZoneId

class MainActivity : ComponentActivity() {

    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        userPreferencesRepository =
            UserPreferencesRepository(dataStore = this@MainActivity.userDataStore)

        val httpClientProvider = ru.kubsu.market.core.network.HttpClientProvider(userPrefs = userPreferencesRepository)
        val httpClient = httpClientProvider.create()
        val authRepository = ru.kubsu.market.core.network.AuthRepositoryImpl(
            httpClient = httpClient,
            userPrefs = userPreferencesRepository
        )

        viewModel =
            viewModels<AppViewModel> {
                AppViewModelFactory(
                    userPreferencesRepository = userPreferencesRepository,
                    authRepository = authRepository,
                    httpClient = httpClient
                )
            }.value

        val loginUseCase = ru.kubsu.market.feature.auth.domain.usecase.LoginUseCase(authRepository = authRepository)
        val authViewModel = viewModels<ru.kubsu.market.feature.auth.presentation.viewmodel.AuthViewModel> {
            ru.kubsu.market.feature.auth.presentation.viewmodel.AuthViewModelFactory(loginUseCase)
        }.value

        setContent {
            LaunchedEffect(Unit) {
                viewModel.startMidnightSchedule(ZoneId.systemDefault())
                viewModel.isCheckedIn.collectLatest {
                    Log.d("CHECKED", "$it")
                }
            }
            val show by viewModel.show.collectAsStateWithLifecycle()
            val isCheckedIn by viewModel.isCheckedIn.collectAsStateWithLifecycle()

            if (show && isCheckedIn) {
                MidnightQuestionDialog(
                    question = "Подтвердить отчёт за день?",
                    secondsToAnswer = 60 * 5,
                    onYes = {
                        viewModel.onEvent(ScreenEvent.OnZeroOvertimeCheckOut)
                        viewModel.dismiss()
                    },
                    onNo = { viewModel.dismiss() },
                    onTimeout = { viewModel.dismiss() }
                )
            }

            BackHandler { viewModel.onEvent(OnBack) }

            val pricesByProductId by viewModel.pricesByProductId.collectAsStateWithLifecycle()

            val state by viewModel.state.collectAsStateWithLifecycle()
            val isAuth = state is ScreenState.Authorization
            val role by viewModel.role.collectAsStateWithLifecycle()

            val padding by animateDpAsState((if (!isAuth) 15 else 0).dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Colors.BLACK)
                    .padding(padding)
            ) {
                AnimatedVisibility(
                    visible = !isAuth
                ) { LogoWithRole(role) }

                Crossfade(
                    targetState = state
                ) { stateValue ->
                    when (stateValue) {
                        ScreenState.Authorization -> AuthScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {}
                        )
                        is ScreenState.Items -> ItemsRepresentationScreen(
                            items = stateValue.items,
                            className = stateValue.className
                        )

                        is ScreenState.Products -> ItemsRepresentationScreen(
                            items = stateValue.items,
                            className = Product.className,
                            container = { item ->
                                ProductRepresentationCard(
                                    product = item as Product,
                                    priceInfo = stateValue.prices.get(
                                        item.productId
                                    )
                                )
                            }
                        )

                        ScreenState.MainMenu -> {
                            viewModel.clearStack()
                            val orderFormed by viewModel.orderFormed.collectAsStateWithLifecycle()
                            val role by viewModel.role.collectAsStateWithLifecycle()
                            val isCheckedIn by viewModel.isCheckedIn.collectAsStateWithLifecycle()

                            MainMenuScreen(
                                role = role,
                                isCheckedIn = isCheckedIn,
                                orderFormed = orderFormed,
                                onMenuCategorySelected = { category ->
                                    viewModel.onEvent(ScreenEvent.OnMenuCategorySelected(category))
                                },
                                onCheckInOut = { isChecked ->
                                    viewModel.onEvent(if (isChecked) ScreenEvent.OnCheckOut else ScreenEvent.OnCheckIn)
                                },
                                onFormOrder = {
                                    viewModel.onEvent(ScreenEvent.OnFormOrder)
                                }
                            )
                        }

                        ScreenState.Loading -> LoadingScreen(modifier = Modifier.weight(1f))
                        is ScreenState.Me -> ShiftScreen(
                            employee = stateValue.me,
                            role = stateValue.role,
                            hours = stateValue.hours,
                            underwork = stateValue.underwork,
                            overwork = stateValue.overwork,
                            vacation = stateValue.vacation,
                            onLogOut = { viewModel.onEvent(ScreenEvent.OnLogOut) },
                            onVacationRequested = { viewModel.onEvent(ScreenEvent.OnVacationRequested(it)) },
                            onReportsRequested = { id ->
                                viewModel.onEvent(ScreenEvent.OnReportsRequestedForEmployee(employeeId = id))
                            }
                        )

                        is ScreenState.ResolveProducts -> ReceivalScreen(
                            toResolveList = stateValue.toResolveProducts,
                            onProductsResolved = { accepted, refused ->
                                viewModel.onEvent(
                                    ScreenEvent.OnProductsResolved(
                                        acceptedProducts = accepted,
                                        refusedProducts = refused
                                    )
                                )
                            }
                        )

                        is ScreenState.Storage -> ItemsRepresentationScreen(
                            items = stateValue.items,
                            className = StorageLocation.className,
                            container = {
                                ItemRepresentationCardExpanded(item = it, onDelete = {}) { item ->
                                    viewModel.onEvent(
                                        OnShelvesForStorageLocationRequested(
                                            (item as StorageLocation).storageLocationId!!
                                        )
                                    )
                                }
                            },
                            addDialog = { onDismiss ->
                                StorageLocationEditDialog(
                                    onConfirm = {},
                                    onDismiss = onDismiss
                                )
                            }
                        )

                        is ScreenState.Shelves -> ItemsRepresentationScreen(
                            items = stateValue.items,
                            className = Shelf.className,
                            container = {
                                ItemRepresentationCardExpanded(item = it, onDelete = {}) { item ->
                                    viewModel.onEvent(
                                        OnProductsForShelfRequested(
                                            (item as Shelf).shelfId!!
                                        )
                                    )
                                }
                            },
                            addDialog = { onDismiss ->
                                ShelfEditDialog(
                                    storageLocations = stateValue.storageLocations,
                                    onConfirm = {},
                                    onDismiss = onDismiss
                                )
                            }
                        )

                        is ScreenState.Employees -> {
                            val mappedState = when (stateValue) {
                                is ScreenState.Employees.Employees -> EmployeesScreenState.Employees(
                                    employees = stateValue.employees,
                                    positions = stateValue.positions
                                )
                                is ScreenState.Employees.Vacations -> EmployeesScreenState.Vacations(
                                    vacations = stateValue.vacations
                                )
                                ScreenState.Employees.Loading -> EmployeesScreenState.Loading
                            }
                            EmployeeScreen(
                                state = mappedState,
                                onTabSelected = { isVacations ->
                                    viewModel.onEvent(if (isVacations) ScreenEvent.OnVacationsRequested else ScreenEvent.OnEmployeesRequested)
                                },
                                onDeleteEmployee = { id ->
                                    viewModel.onEvent(ScreenEvent.OnDeleteEmployee(id))
                                },
                                onAddEmployee = { emp ->
                                    viewModel.onEvent(ScreenEvent.OnAddEmployee(emp))
                                },
                                onVacationResponse = { vac ->
                                    viewModel.onEvent(ScreenEvent.OnVacationResponseGiven(vac))
                                }
                            )
                        }

                        is ScreenState.Reports ->
                            ItemsRepresentationScreen(
                                items = stateValue.reports,
                                className = PersonnelReport.className,
                                buttonText = "Утвердить отчёты",
                                buttonEnabled = stateValue.reports.isNotEmpty(),
                                onButtonClick = {
                                    viewModel.onEvent(
                                        ScreenEvent.OnReportsConfirm
                                    )
                                },
                                container = { report ->
                                    ReportRepresentationCard(
                                        report = report as PersonnelReport,
                                        onEdit = { cRequest ->
                                            viewModel.onEvent(
                                                OnUpdateReport(
                                                    reports = stateValue.reports,
                                                    reportId = report.personnelReportId!!,
                                                    request = cRequest
                                                )
                                            )
                                        }
                                    )
                                }
                            )

                        ScreenState.Dictionaries -> DictionariesScreen(fetcher = viewModel)
                    }
                }
            }
        }

        // больше кринжа
        lifecycleScope.launch {
            viewModel.errorEvents.collectLatest {
                it?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    viewModel.clearErrorEvents()
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.refresh()
    }
}