package ru.kubsu.market.core.ui.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun FieldsRepresentation(
    modifier: Modifier = Modifier,
    map: Map<String, String>
) = Column(
    modifier = modifier
) {
    map.entries.forEach { info ->
        Text(
            modifier = Modifier.basicMarquee(),
            maxLines = 1,
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Colors.WHITE
                    )
                ) {
                    append("${info.key}: ")
                }

                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = Colors.LIGHT_GRAY
                    )
                ) {
                    append(info.value)
                }
            },
            fontSize = 13.sp,
        )
    }
}
