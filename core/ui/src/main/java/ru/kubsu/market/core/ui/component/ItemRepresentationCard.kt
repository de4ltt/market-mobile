package ru.kubsu.market.core.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.model.UiDisplayable
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ItemRepresentationCard(
    item: UiDisplayable
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
            .clickable(
                onClick = { isExpanded = !isExpanded },
                indication = null,
                interactionSource = null
            )
    ) {
        AnimatedContent(
            targetState = isExpanded,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Top)
        ) { expanded ->
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item.barcode?.let { BarCode(code = it) }
                    FieldsRepresentation(map = item.displayFields)
                }
            } else {
                Text(
                    text = item.displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.WHITE
                )
            }
        }
    }
}
