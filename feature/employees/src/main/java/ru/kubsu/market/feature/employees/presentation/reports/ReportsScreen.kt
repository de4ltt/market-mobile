package ru.kubsu.market.feature.employees.presentation.reports

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    employeeId: Int? = null
) {
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(employeeId) {
        if (employeeId != null) {
            viewModel.loadEmployeeReports(employeeId)
        } else {
            viewModel.loadReports()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val stateValue = state) {
            is ReportsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.LIGHT_BLUE
                )
            }
            is ReportsUiState.Success -> {
                if (employeeId == null) {
                    ItemsRepresentationScreen(
                        items = stateValue.reports,
                        className = PersonnelReport.className,
                        buttonText = "Утвердить отчёты",
                        buttonEnabled = stateValue.reports.isNotEmpty(),
                        onButtonClick = {
                            viewModel.confirmWeeklyReports()
                        },
                        container = { report ->
                            ReportRepresentationCard(
                                report = report as PersonnelReport,
                                onEdit = { cRequest ->
                                    viewModel.updateReport(report.personnelReportId!!, cRequest)
                                }
                            )
                        }
                    )
                } else {
                    ItemsRepresentationScreen(
                        items = stateValue.reports,
                        className = PersonnelReport.className,
                        container = { report ->
                            ReportRepresentationCard(
                                report = report as PersonnelReport,
                                onEdit = { cRequest ->
                                    viewModel.updateReport(report.personnelReportId!!, cRequest)
                                }
                            )
                        }
                    )
                }
            }
            is ReportsUiState.Error -> {
                Text(
                    text = stateValue.message,
                    color = Colors.RED,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
