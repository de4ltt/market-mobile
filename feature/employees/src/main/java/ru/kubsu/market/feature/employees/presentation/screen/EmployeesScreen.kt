package ru.kubsu.market.feature.employees.presentation.screen

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.ItemRepresentationCardExpanded
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.feature.employees.presentation.component.VacationRepresentationCard
import ru.kubsu.market.feature.employees.presentation.component.EmployeeEditDialog
import ru.kubsu.market.feature.employees.presentation.model.EmployeesScreenState
import ru.kubsu.market.feature.employees.presentation.model.EmployeesUiEvent
import ru.kubsu.market.feature.employees.presentation.viewmodel.EmployeesViewModel
import ru.kubsu.market.core.ui.mapping.toUiDisplayable

@Composable
fun EmployeesRoute(
    viewModel: EmployeesViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EmployeesUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    EmployeeScreen(
        state = state,
        onTabSelected = { isVacations ->
            if (isVacations) viewModel.loadVacations() else viewModel.loadEmployees()
        },
        onDeleteEmployee = viewModel::deleteEmployee,
        onAddEmployee = viewModel::addEmployee,
        onVacationResponse = { vacation ->
            viewModel.respondToVacation(vacation.vacationId!!, vacation.approved)
        },
        onRetry = {
            // Во ViewModel при ретро-вызове само вызовет нужный метод
            if (state is EmployeesScreenState.Error) {
                // Если произошла ошибка при загрузке отпусков, повторим загрузку отпусков, иначе сотрудников
                viewModel.loadEmployees()
            }
        },
        modifier = modifier
    )
}

@Composable
fun EmployeeScreen(
    state: EmployeesScreenState,
    onTabSelected: (isVacations: Boolean) -> Unit,
    onDeleteEmployee: (Int) -> Unit,
    onAddEmployee: (Employee) -> Unit,
    onVacationResponse: (Vacation) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val employeesTextColor by animateColorAsState(
        if (state is EmployeesScreenState.Employees || (state is EmployeesScreenState.Error && !state.message.contains("отпуск"))) Colors.WHITE else Colors.LIGHT_GRAY
    )
    val vacationsTextColor by animateColorAsState(
        if (state is EmployeesScreenState.Vacations || (state is EmployeesScreenState.Error && state.message.contains("отпуск"))) Colors.WHITE else Colors.LIGHT_GRAY
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Text(
                modifier = Modifier.clickable(
                    onClick = { onTabSelected(false) },
                    interactionSource = null,
                    indication = null
                ),
                text = "Сотрудники",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = employeesTextColor
            )

            Text(
                modifier = Modifier.clickable(
                    onClick = { onTabSelected(true) },
                    interactionSource = null,
                    indication = null
                ),
                text = "Отпуска",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = vacationsTextColor
            )
        }

        Crossfade(targetState = state, modifier = Modifier.weight(1f)) { stateVal ->
            when (stateVal) {
                is EmployeesScreenState.Employees -> ItemsRepresentationScreen(
                    items = stateVal.employees,
                    container = { item ->
                        ItemRepresentationCardExpanded(item = item.toUiDisplayable(), onDelete = {
                            onDeleteEmployee(item.employeeId!!)
                        })
                    },
                    addDialog = { onDismiss ->
                        EmployeeEditDialog(
                            positions = stateVal.positions,
                            onConfirm = { employee ->
                                onAddEmployee(employee)
                            },
                            onDismiss = onDismiss
                        )
                    }
                )

                EmployeesScreenState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Colors.DARK_BLUE) }

                is EmployeesScreenState.Vacations -> ItemsRepresentationScreen(
                    items = stateVal.vacations,
                    container = { vacation ->
                        VacationRepresentationCard(
                            vacation = vacation as Vacation,
                            onAccept = {
                                onVacationResponse(
                                    vacation.copy(
                                        reviewed = true,
                                        approved = true
                                    )
                                )
                            },
                            onRefuse = {
                                onVacationResponse(
                                    vacation.copy(
                                        reviewed = true,
                                        approved = false
                                    )
                                )
                            }
                        )
                    }
                )

                is EmployeesScreenState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stateVal.message,
                            color = Colors.RED,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        AppButton(
                            text = "Повторить",
                            onClick = onRetry
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun EmployeeScreenLoadingPreview() {
    EmployeeScreen(
        state = EmployeesScreenState.Loading,
        onTabSelected = {},
        onAddEmployee = {},
        onDeleteEmployee = {},
        onVacationResponse = {},
        onRetry = {}
    )
}
