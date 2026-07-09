package ru.kubsu.market.feature.employees.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.component.FieldsRepresentation
import ru.kubsu.market.core.ui.theme.Colors
import ru.kubsu.market.core.ui.mapping.toUiDisplayable

@Composable
fun VacationRepresentationCard(
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
    FieldsRepresentation(map = vacation.toUiDisplayable().displayFields)

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
