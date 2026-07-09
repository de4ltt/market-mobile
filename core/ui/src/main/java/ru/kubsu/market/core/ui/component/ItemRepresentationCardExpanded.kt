package ru.kubsu.market.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.ui.model.UiDisplayable
import ru.kubsu.market.core.ui.R as CoreUiR
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ItemRepresentationCardExpanded(
    item: UiDisplayable,
    onDelete: (() -> Unit)? = null,
    onClick: () -> Unit = {},
) = Box(
    modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(color = Colors.DARK_GRAY)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(20.dp)
        .clickable(
            onClick = onClick,
            indication = null,
            interactionSource = null
        )
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item.barcode?.let { BarCode(code = it) }
        FieldsRepresentation(map = item.displayFields)
    }

    onDelete?.let {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = onDelete,
                    indication = null,
                    interactionSource = null
                ),
            painter = painterResource(CoreUiR.drawable.bin),
            tint = Colors.RED,
            contentDescription = "delete_icon"
        )
    }
}
