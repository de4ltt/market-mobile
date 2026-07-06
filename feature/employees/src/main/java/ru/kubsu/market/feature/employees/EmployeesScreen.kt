package ru.kubsu.market.feature.employees

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.ItemsRepresentationScreen
import ru.kubsu.market.core.model.ItemRepresentationCardExpanded
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.feature.employees.component.EmployeeEditDialog

sealed interface EmployeesScreenState {
    data class Employees(val employees: List<Employee>, val positions: List<Position>) : EmployeesScreenState
    data class Vacations(val vacations: List<Vacation>) : EmployeesScreenState
    data object Loading : EmployeesScreenState
}

@Composable
fun EmployeeScreen(
    state: EmployeesScreenState,
    onTabSelected: (isVacations: Boolean) -> Unit,
    onDeleteEmployee: (Int) -> Unit,
    onAddEmployee: (Employee) -> Unit,
    onVacationResponse: (Vacation) -> Unit
) {
    val employeesTextColor by animateColorAsState(if (state is EmployeesScreenState.Employees) Colors.WHITE else Colors.LIGHT_GRAY)
    val vacationsTextColor by animateColorAsState(if (state is EmployeesScreenState.Vacations) Colors.WHITE else Colors.LIGHT_GRAY)

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
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
                        ItemRepresentationCardExpanded(item = item, onDelete = {
                            onDeleteEmployee((item as Employee).employeeId!!)
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
            }
        }
    }
}

@Composable
private fun VacationRepresentationCard(
    vacation: Vacation,
    onAccept: () -> Unit,
    onRefuse: () -> Unit
) = Column(
    modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(color = Colors.DARK_GRAY)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(15.dp)
) {
    vacation.FullContent()

    if (!vacation.reviewed) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            AppButton(
                modifier = Modifier.weight(1f),
                text = "Отказать",
                buttonType = AppButtonType.NEGATIVE,
                onClick = onRefuse
            )

            AppButton(
                modifier = Modifier.weight(1f),
                text = "Одобрить",
                buttonType = AppButtonType.POSITIVE,
                onClick = onAccept
            )
        }
    }
}
