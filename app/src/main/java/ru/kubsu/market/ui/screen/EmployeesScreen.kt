package ru.kubsu.market.ui.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.ui.component.EmployeeEditDialog
import ru.kubsu.market.ui.cringe.ScreenEvent
import ru.kubsu.market.ui.cringe.ScreenState
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun EmployeeScreen(
    state: ScreenState.Employees,
    onEvent: (ScreenEvent) -> Unit
) {

    val employeesTextColor by animateColorAsState(if (state is ScreenState.Employees.Employees) Colors.WHITE else Colors.LIGHT_GRAY)
    val vacationsTextColor by animateColorAsState(if (state is ScreenState.Employees.Vacations) Colors.WHITE else Colors.LIGHT_GRAY)

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Text(
                modifier = Modifier.clickable(
                    onClick = { onEvent(ScreenEvent.OnEmployeesRequested) },
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
                    onClick = { onEvent(ScreenEvent.OnVacationsRequested) },
                    interactionSource = null,
                    indication = null
                ),
                text = "Отпуска",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = vacationsTextColor
            )
        }

        Crossfade(targetState = state, modifier = Modifier.weight(1f)) {
            when (val state = it) {
                is ScreenState.Employees.Employees -> ItemsRepresentationScreen(
                    items = state.employees,
                    container = { item ->
                        ItemRepresentationCardExpanded(item = item, onDelete = {
                            onEvent(ScreenEvent.OnDeleteEmployee((item as Employee).employeeId!!))
                        })
                    },
                    addDialog = { onDismiss ->
                        EmployeeEditDialog(
                            positions = state.positions,
                            onConfirm = { employee ->
                                onEvent(ScreenEvent.OnAddEmployee(employee))
                            },
                            onDismiss = onDismiss
                        )
                    }
                )

                ScreenState.Employees.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Colors.DARK_BLUE) }

                is ScreenState.Employees.Vacations -> ItemsRepresentationScreen(
                    items = state.vacations,
                    container = { vacation ->
                        VacationRepresentationCard(
                            vacation = vacation as Vacation,
                            onAccept = {
                                onEvent(
                                    ScreenEvent.OnVacationResponseGiven(
                                        vacation.copy(
                                            reviewed = true,
                                            approved = true
                                        )
                                    )
                                )
                            },
                            onRefuse = {
                                onEvent(
                                    ScreenEvent.OnVacationResponseGiven(
                                        vacation.copy(
                                            reviewed = true,
                                            approved = false
                                        )
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