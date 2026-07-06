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
import ru.kubsu.market.ui.component.ShelfEditDialog
import ru.kubsu.market.ui.component.StorageLocationEditDialog
import ru.kubsu.market.feature.products.presentation.screen.ProductsScreen
import ru.kubsu.market.feature.products.presentation.screen.StorageScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelvesScreen
import ru.kubsu.market.feature.products.presentation.screen.ShelfProductsScreen
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
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.Duration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kubsu.market.core.network.SessionState
import ru.kubsu.market.core.model.MenuCategory
import ru.kubsu.market.ui.navigation.Destination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
            var showMidnightQuestion by remember { mutableStateOf(false) }

            LaunchedEffect(isCheckedIn) {
                if (isCheckedIn) {
                    while (true) {
                        val delayMs = millisUntilNextMidnight(ZoneId.systemDefault())
                        kotlinx.coroutines.delay(delayMs)
                        showMidnightQuestion = true
                        kotlinx.coroutines.delay(1000)
                    }
                } else {
                    showMidnightQuestion = false
                }
            }

            if (showMidnightQuestion && isCheckedIn) {
                MidnightQuestionDialog(
                    question = "Подтвердить отчёт за день?",
                    secondsToAnswer = 60 * 5,
                    onYes = {
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        loggedIn?.let {
                            lifecycleScope.launch {
                                try {
                                    sessionManager.zeroOvertimeCheckOut(it.userId)
                                    showMidnightQuestion = false
                                } catch (e: Exception) {
                                    Toast.makeText(this@MainActivity, e.message ?: "Ошибка операции", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    onNo = { showMidnightQuestion = false },
                    onTimeout = { showMidnightQuestion = false }
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
                        AuthScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {}
                        )
                    }

                    composable<Destination.MainMenu> {
                        val orderFormed by productsViewModel.orderFormed.collectAsStateWithLifecycle()
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        val role = loggedIn?.role
                        val userId = loggedIn?.userId ?: 0

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

                    composable<Destination.Products> {
                        LaunchedEffect(Unit) {
                            productsViewModel.loadProducts()
                        }
                        ProductsScreen(viewModel = productsViewModel)
                    }

                    composable<Destination.ResolveProducts> {
                        val loggedIn = sessionState as? SessionState.LoggedIn
                        val userId = loggedIn?.userId ?: 0
                        LaunchedEffect(Unit) {
                            receivalViewModel.loadProducts()
                        }
                        ReceivalScreen(
                            viewModel = receivalViewModel,
                            employeeId = userId,
                            onFinished = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<Destination.Storage> {
                        LaunchedEffect(Unit) {
                            productsViewModel.loadStorageLocations()
                        }
                        StorageScreen(
                            viewModel = productsViewModel,
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
                        LaunchedEffect(shelvesRoute.storageLocationId) {
                            productsViewModel.loadShelves(shelvesRoute.storageLocationId)
                        }
                        val productsState by productsViewModel.uiState.collectAsStateWithLifecycle()
                        val storageLocations = (productsState as? ru.kubsu.market.feature.products.presentation.viewmodel.ProductsUiState.Shelves)?.storageLocations ?: emptyList()
                        ShelvesScreen(
                            viewModel = productsViewModel,
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
                        LaunchedEffect(routeData.shelfId) {
                            productsViewModel.loadProductsForShelf(routeData.shelfId)
                        }
                        ShelfProductsScreen(viewModel = productsViewModel)
                    }

                    composable<Destination.Employees> {
                        LaunchedEffect(Unit) {
                            employeesViewModel.loadEmployees()
                        }
                        EmployeesScreen(viewModel = employeesViewModel)
                    }

                    composable<Destination.Me> { backStackEntry ->
                        val routeData = backStackEntry.toRoute<Destination.Me>()
                        LaunchedEffect(routeData.employeeId) {
                            shiftViewModel.loadProfile(routeData.employeeId)
                        }
                        ShiftScreen(
                            viewModel = shiftViewModel,
                            onLogOut = {
                                productsViewModel.clearOrderFormed()
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
                        ru.kubsu.market.feature.employees.presentation.reports.ReportsScreen(
                            viewModel = reportsViewModel,
                            employeeId = routeData.employeeId
                        )
                    }

                    composable<Destination.Dictionaries> {
                        DictionariesScreen(viewModel = dictionariesViewModel)
                    }
                }
            }
        }
    }

    private fun millisUntilNextMidnight(zoneId: ZoneId): Long {
        val now = ZonedDateTime.now(zoneId)
        val nextMidnight = now
            .truncatedTo(ChronoUnit.DAYS)
            .plusDays(1)
        return Duration.between(now, nextMidnight).toMillis().coerceAtLeast(0)
    }
}