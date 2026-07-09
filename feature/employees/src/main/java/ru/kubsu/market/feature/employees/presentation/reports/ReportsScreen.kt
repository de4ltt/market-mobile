package ru.kubsu.market.feature.employees.presentation.reports

import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.ui.component.ItemsRepresentationScreen
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ReportsRoute(
    viewModel: ReportsViewModel,
    employeeId: Int? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(employeeId) {
        if (employeeId != null) {
            viewModel.loadEmployeeReports(employeeId)
        } else {
            viewModel.loadReports()
        }
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ReportsUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ReportsScreen(
        state = state,
        employeeId = employeeId,
        onConfirmReports = viewModel::confirmWeeklyReports,
        onUpdateReport = viewModel::updateReport,
        modifier = modifier
    )
}

@Composable
fun ReportsScreen(
    state: ReportsUiState,
    employeeId: Int?,
    onConfirmReports: () -> Unit,
    onUpdateReport: (Int, ConfirmReportRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
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
                        onButtonClick = onConfirmReports,
                        container = { report ->
                            ReportRepresentationCard(
                                report = report,
                                onEdit = { cRequest ->
                                    onUpdateReport(report.personnelReportId!!, cRequest)
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
                                report = report,
                                onEdit = { cRequest ->
                                    onUpdateReport(report.personnelReportId!!, cRequest)
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun ReportsScreenLoadingPreview() {
    ReportsScreen(
        state = ReportsUiState.Loading,
        employeeId = null,
        onConfirmReports = {},
        onUpdateReport = { _, _ -> }
    )
}
