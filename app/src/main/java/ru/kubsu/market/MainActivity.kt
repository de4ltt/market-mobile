package ru.kubsu.market

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import javax.inject.Inject
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
import ru.kubsu.market.ui.component.ShelfEditDialog
import ru.kubsu.market.ui.component.StorageLocationEditDialog
import ru.kubsu.market.feature.products.presentation.screen.ProductsScreen
import ru.kubsu.market.feature.products.presentation.screen.StorageScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelvesScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelfProductsScreen
import ru.kubsu.market.ui.cringe.AppViewModel
import ru.kubsu.market.ui.cringe.ScreenEvent
import ru.kubsu.market.ui.cringe.ScreenEvent.OnBack
import ru.kubsu.market.ui.cringe.ScreenEvent.OnProductsForShelfRequested
import ru.kubsu.market.ui.cringe.ScreenEvent.OnShelvesForStorageLocationRequested
import ru.kubsu.market.ui.cringe.ScreenState
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
import ru.kubsu.market.feature.receival.presentation.viewmodel.ReceivalViewModel
import ru.kubsu.market.feature.dictionaries.presentation.viewmodel.DictionariesViewModel
import ru.kubsu.market.feature.shift.ShiftScreen
import ru.kubsu.market.feature.employees.EmployeesScreen
import ru.kubsu.market.feature.employees.presentation.viewmodel.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.kubsu.market.core.ui.theme.Colors
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()
    private val productsViewModel: ru.kubsu.market.feature.products.presentation.viewmodel.ProductsViewModel by viewModels()
    private val authViewModel: ru.kubsu.market.feature.auth.presentation.viewmodel.AuthViewModel by viewModels()
    private val shiftViewModel: ru.kubsu.market.feature.shift.presentation.viewmodel.ShiftViewModel by viewModels()
    private val receivalViewModel: ReceivalViewModel by viewModels()
    private val dictionariesViewModel: DictionariesViewModel by viewModels()
    private val employeesViewModel: EmployeesViewModel by viewModels()
    private val reportsViewModel: ru.kubsu.market.feature.employees.presentation.reports.ReportsViewModel by viewModels()

    @Inject
    lateinit var sessionManager: ru.kubsu.market.core.network.SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
                        is ScreenState.ShelfProducts -> {
                            LaunchedEffect(stateValue.shelfId) {
                                productsViewModel.loadProductsForShelf(stateValue.shelfId)
                            }
                            ShelfProductsScreen(viewModel = productsViewModel)
                        }

                        ScreenState.Products -> {
                            LaunchedEffect(Unit) {
                                productsViewModel.loadProducts()
                            }
                            ProductsScreen(viewModel = productsViewModel)
                        }

                        ScreenState.MainMenu -> {
                            viewModel.clearStack()
                            val orderFormed by productsViewModel.orderFormed.collectAsStateWithLifecycle()
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
                                    productsViewModel.formOrder(viewModel.id ?: 0)
                                }
                            )
                        }

                        ScreenState.Loading -> LoadingScreen(modifier = Modifier.weight(1f))
                        is ScreenState.Me -> {
                            LaunchedEffect(stateValue.me.employeeId) {
                                shiftViewModel.loadProfile(stateValue.me.employeeId!!)
                            }
                            ShiftScreen(
                                viewModel = shiftViewModel,
                                onLogOut = {
                                    productsViewModel.clearOrderFormed()
                                    viewModel.onEvent(ScreenEvent.OnLogOut)
                                },
                                onReportsRequested = { id ->
                                    viewModel.navigateTo(ScreenState.Reports(employeeId = id))
                                }
                            )
                        }

                        ScreenState.ResolveProducts -> {
                            LaunchedEffect(Unit) {
                                receivalViewModel.loadProducts()
                            }
                            ReceivalScreen(
                                viewModel = receivalViewModel,
                                employeeId = viewModel.id ?: 0,
                                onFinished = {
                                    viewModel.onEvent(OnBack)
                                }
                            )
                        }

                        ScreenState.Storage -> {
                            LaunchedEffect(Unit) {
                                productsViewModel.loadStorageLocations()
                            }
                            StorageScreen(
                                viewModel = productsViewModel,
                                onShelvesRequested = { storageLocationId ->
                                    viewModel.onEvent(OnShelvesForStorageLocationRequested(storageLocationId))
                                },
                                addDialog = { onDismiss ->
                                    StorageLocationEditDialog(
                                        onConfirm = {},
                                        onDismiss = onDismiss
                                    )
                                }
                            )
                        }

                        is ScreenState.Shelves -> {
                            LaunchedEffect(stateValue.storageLocationId) {
                                productsViewModel.loadShelves(stateValue.storageLocationId)
                            }
                            val productsState by productsViewModel.uiState.collectAsStateWithLifecycle()
                            val storageLocations = (productsState as? ru.kubsu.market.feature.products.presentation.viewmodel.ProductsUiState.Shelves)?.storageLocations ?: emptyList()
                            ShelvesScreen(
                                viewModel = productsViewModel,
                                onProductsRequested = { shelfId ->
                                    viewModel.onEvent(OnProductsForShelfRequested(shelfId))
                                },
                                addDialog = { onDismiss ->
                                    ShelfEditDialog(
                                        storageLocations = storageLocations,
                                        onConfirm = {},
                                        onDismiss = onDismiss
                                    )
                                }
                            )
                        }

                        ScreenState.Employees -> {
                            LaunchedEffect(Unit) {
                                employeesViewModel.loadEmployees()
                            }
                            EmployeesScreen(viewModel = employeesViewModel)
                        }

                        is ScreenState.Reports -> {
                            ru.kubsu.market.feature.employees.presentation.reports.ReportsScreen(
                                viewModel = reportsViewModel,
                                employeeId = stateValue.employeeId
                            )
                        }

                        ScreenState.Dictionaries -> DictionariesScreen(viewModel = dictionariesViewModel)
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