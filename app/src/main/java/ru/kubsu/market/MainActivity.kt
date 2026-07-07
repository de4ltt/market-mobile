package ru.kubsu.market

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import javax.inject.Inject
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.ui.component.LogoWithRole
import ru.kubsu.market.ui.component.MidnightQuestionDialog
import ru.kubsu.market.feature.products.presentation.component.ShelfEditDialog
import ru.kubsu.market.feature.products.presentation.component.StorageLocationEditDialog
import ru.kubsu.market.feature.products.presentation.screen.ProductsScreen
import ru.kubsu.market.feature.products.presentation.screen.StorageScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelvesScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelfProductsScreen
import ru.kubsu.market.feature.auth.presentation.screen.AuthRoute
import ru.kubsu.market.feature.dictionaries.presentation.screen.DictionariesRoute
import ru.kubsu.market.feature.employees.presentation.model.EmployeesScreenState
import ru.kubsu.market.core.ui.component.ItemRepresentationCard
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.ui.screen.LoadingScreen
import ru.kubsu.market.feature.mainmenu.presentation.screen.MainMenuScreen
import ru.kubsu.market.feature.receival.presentation.screen.ReceivalRoute
import ru.kubsu.market.feature.receival.presentation.viewmodel.ReceivalViewModel
import ru.kubsu.market.feature.dictionaries.presentation.viewmodel.DictionariesViewModel
import ru.kubsu.market.feature.shift.presentation.screen.ShiftRoute
import ru.kubsu.market.feature.employees.presentation.screen.EmployeesRoute
import ru.kubsu.market.feature.employees.presentation.viewmodel.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kubsu.market.core.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.Duration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kubsu.market.core.data.SessionState
import ru.kubsu.market.core.model.MenuCategory
import ru.kubsu.market.ui.navigation.Destination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: ru.kubsu.market.presentation.viewmodel.MainActivityViewModel by viewModels()

    @Inject
    lateinit var sessionManager: ru.kubsu.market.core.data.SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val sessionState by sessionManager.sessionState.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            LaunchedEffect(sessionState) {
                when (val currentSession = sessionState) {
                    is SessionState.LoggedOut -> {
                        navController.navigate(Destination.Authorization) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    is SessionState.LoggedIn -> {
                        navController.navigate(Destination.MainMenu) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    is SessionState.Loading -> {
                        // Wait on loading screen
                    }
                }
            }

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntry?.destination
            val isAuth = currentDestination?.hasRoute(Destination.Authorization::class) == true ||
                         currentDestination?.hasRoute(Destination.Loading::class) == true

            val userRole = (sessionState as? SessionState.LoggedIn)?.role

            // Midnight question scheduling
            val isCheckedIn by sessionManager.isCheckedIn.collectAsStateWithLifecycle()
            val showMidnightQuestion by mainViewModel.showMidnightQuestion.collectAsStateWithLifecycle()

            if (showMidnightQuestion && isCheckedIn) {
                MidnightQuestionDialog(
                    question = "Подтвердить отчёт за день?",
                    secondsToAnswer = 60 * 5,
                    onYes = {
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        loggedIn?.let {
                            try {
                                mainViewModel.onZeroOvertimeChecked(it.userId)
                            } catch (e: Exception) {
                                Toast.makeText(this@MainActivity, e.message ?: "Ошибка операции", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onNo = { mainViewModel.dismissMidnightQuestion() },
                    onTimeout = { mainViewModel.dismissMidnightQuestion() }
                )
            }

            val padding by animateDpAsState(if (!isAuth) 15.dp else 0.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Colors.BLACK)
                    .padding(padding)
            ) {
                AnimatedVisibility(visible = !isAuth) {
                    LogoWithRole(userRole)
                }

                NavHost(
                    navController = navController,
                    startDestination = Destination.Loading,
                    modifier = Modifier.weight(1f)
                ) {
                    composable<Destination.Loading> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }

                    composable<Destination.Authorization> {
                        val authViewModel = hiltViewModel<ru.kubsu.market.feature.auth.presentation.viewmodel.AuthViewModel>()
                        AuthRoute(
                            viewModel = authViewModel,
                            onLoginSuccess = {}
                        )
                    }

                    composable<Destination.MainMenu> {
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        val role = loggedIn?.role
                        val userId = loggedIn?.userId ?: 0
                        var orderFormed by remember { mutableStateOf(false) }
                        val productsViewModel = hiltViewModel<ru.kubsu.market.feature.products.presentation.viewmodel.ProductsViewModel>()

                        LaunchedEffect(Unit) {
                            productsViewModel.uiEvent.collectLatest { event ->
                                when (event) {
                                    ru.kubsu.market.feature.products.presentation.model.ProductsUiEvent.OrderFormed -> {
                                        orderFormed = true
                                        Toast.makeText(this@MainActivity, "Приказ успешно выполнен", Toast.LENGTH_SHORT).show()
                                    }
                                    is ru.kubsu.market.feature.products.presentation.model.ProductsUiEvent.ShowToast -> {
                                        Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }

                        MainMenuScreen(
                            role = role,
                            isCheckedIn = isCheckedIn,
                            orderFormed = orderFormed,
                            onMenuCategorySelected = { category ->
                                when (category) {
                                    MenuCategory.PRODUCTS -> navController.navigate(Destination.Products)
                                    MenuCategory.RECEIVAL -> navController.navigate(Destination.ResolveProducts)
                                    MenuCategory.STORAGE -> navController.navigate(Destination.Storage)
                                    MenuCategory.EMPLOYEES -> navController.navigate(Destination.Employees)
                                    MenuCategory.MY_SHIFT -> navController.navigate(Destination.Me(userId))
                                    MenuCategory.REPORT -> navController.navigate(Destination.Reports(employeeId = null))
                                    MenuCategory.DICTIONARIES -> navController.navigate(Destination.Dictionaries)
                                }
                            },
                            onCheckInOut = { isChecked ->
                                lifecycleScope.launch {
                                    try {
                                        if (isChecked) {
                                            sessionManager.checkOut(userId)
                                        } else {
                                            sessionManager.checkIn(userId)
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(this@MainActivity, e.message ?: "Ошибка операции", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            onFormOrder = {
                                productsViewModel.formOrder(userId)
                            }
                        )
                    }

                    composable<Destination.Products> { backStackEntry ->
                        val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Destination.MainMenu) }
                        val productsViewModel = hiltViewModel<ru.kubsu.market.feature.products.presentation.viewmodel.ProductsViewModel>(parentEntry)
                        LaunchedEffect(Unit) {
                            productsViewModel.loadProducts()
                        }
                        ru.kubsu.market.feature.products.presentation.screen.ProductsRoute(viewModel = productsViewModel)
                    }

                    composable<Destination.ResolveProducts> {
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        val userId = loggedIn?.userId ?: 0
                        val receivalViewModel = hiltViewModel<ReceivalViewModel>()
                        LaunchedEffect(Unit) {
                            receivalViewModel.loadProducts()
                        }
                        ReceivalRoute(
                            viewModel = receivalViewModel,
                            employeeId = userId,
                            onFinished = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<Destination.Storage> {
                        val storageViewModel = hiltViewModel<ru.kubsu.market.feature.products.presentation.viewmodel.StorageViewModel>()
                        LaunchedEffect(Unit) {
                            storageViewModel.loadStorageLocations()
                        }
                        ru.kubsu.market.feature.products.presentation.screen.StorageRoute(
                            viewModel = storageViewModel,
                            onShelvesRequested = { storageLocationId ->
                                navController.navigate(Destination.Shelves(storageLocationId))
                            },
                            addDialog = { onDismiss ->
                                StorageLocationEditDialog(
                                    onConfirm = {},
                                    onDismiss = onDismiss
                                )
                            }
                        )
                    }

                    composable<Destination.Shelves> { backStackEntry ->
                        val shelvesRoute = backStackEntry.toRoute<Destination.Shelves>()
                        val shelvesViewModel = hiltViewModel<ru.kubsu.market.feature.products.presentation.viewmodel.ShelvesViewModel>()
                        LaunchedEffect(shelvesRoute.storageLocationId) {
                            shelvesViewModel.loadShelves(shelvesRoute.storageLocationId)
                        }
                        val shelvesState by shelvesViewModel.uiState.collectAsStateWithLifecycle()
                        val storageLocations = (shelvesState as? ru.kubsu.market.feature.products.presentation.model.ShelvesUiState.Success)?.storageLocations ?: emptyList()
                        ru.kubsu.market.feature.products.presentation.screen.ShelvesRoute(
                            viewModel = shelvesViewModel,
                            storageLocationId = shelvesRoute.storageLocationId,
                            onProductsRequested = { shelfId ->
                                navController.navigate(Destination.ShelfProducts(shelfId))
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

                    composable<Destination.ShelfProducts> { backStackEntry ->
                        val routeData = backStackEntry.toRoute<Destination.ShelfProducts>()
                        val shelfProductsViewModel = hiltViewModel<ru.kubsu.market.feature.products.presentation.viewmodel.ShelfProductsViewModel>()
                        LaunchedEffect(routeData.shelfId) {
                            shelfProductsViewModel.loadProductsForShelf(routeData.shelfId)
                        }
                        ru.kubsu.market.feature.products.presentation.screen.ShelfProductsRoute(
                            viewModel = shelfProductsViewModel,
                            shelfId = routeData.shelfId
                        )
                    }

                    composable<Destination.Employees> {
                        val employeesViewModel = hiltViewModel<EmployeesViewModel>()
                        LaunchedEffect(Unit) {
                            employeesViewModel.loadEmployees()
                        }
                        EmployeesRoute(viewModel = employeesViewModel)
                    }

                    composable<Destination.Me> { backStackEntry ->
                        val routeData = backStackEntry.toRoute<Destination.Me>()
                        val shiftViewModel = hiltViewModel<ru.kubsu.market.feature.shift.presentation.viewmodel.ShiftViewModel>()
                        LaunchedEffect(routeData.employeeId) {
                            shiftViewModel.loadProfile(routeData.employeeId)
                        }
                        ShiftRoute(
                            viewModel = shiftViewModel,
                            onLogOut = {
                                lifecycleScope.launch {
                                    sessionManager.logout()
                                }
                            },
                            onReportsRequested = { id ->
                                navController.navigate(Destination.Reports(employeeId = id))
                            }
                        )
                    }

                    composable<Destination.Reports> { backStackEntry ->
                        val routeData = backStackEntry.toRoute<Destination.Reports>()
                        val reportsViewModel = hiltViewModel<ru.kubsu.market.feature.employees.presentation.reports.ReportsViewModel>()
                        ru.kubsu.market.feature.employees.presentation.reports.ReportsRoute(
                            viewModel = reportsViewModel,
                            employeeId = routeData.employeeId
                        )
                    }
                    composable<Destination.Dictionaries> {
                        val dictionariesViewModel = hiltViewModel<DictionariesViewModel>()
                        DictionariesRoute(viewModel = dictionariesViewModel)
                    }
                }
            }
        }
    }
}