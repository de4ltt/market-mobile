package ru.kubsu.market.feature.employees.presentation.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ReportRepresentationCard(
    report: PersonnelReport,
    onEdit: (ConfirmReportRequest) -> Unit,
) {

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    if (isDialogOpen)
        ConfirmReportRequestDialog(
            onDismiss = { isDialogOpen = false },
            onConfirm = { onEdit(it) }
        )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
    ) {
        ru.kubsu.market.core.ui.component.FieldsRepresentation(map = report.toUiDisplayable().displayFields)

        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = { isDialogOpen = true },
                    indication = null,
                    interactionSource = null
                ),
            painter = painterResource(ru.kubsu.market.core.ui.R.drawable.pencil),
            tint = Colors.LIGHT_BLUE,
            contentDescription = "delete_icon"
        )
    }
}
