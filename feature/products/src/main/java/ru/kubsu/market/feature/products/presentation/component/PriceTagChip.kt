package ru.kubsu.market.feature.products.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.model.pricing.PriceFormationResult
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun PriceTagChip(
    price: BigDecimal,
    regularPrice: BigDecimal,
    type: PriceFormationResult.PriceTagType,
    modifier: Modifier = Modifier
) {
    val (bg, fg, title) = when (type) {
        PriceFormationResult.PriceTagType.WHITE -> Triple(Color.White, Color.Black, "БЕЛЫЙ")
        PriceFormationResult.PriceTagType.YELLOW -> Triple(Color(0xFFFFE082), Color.Black, "ЖЁЛТЫЙ")
        PriceFormationResult.PriceTagType.ACTION -> Triple(Color(0xFFEF5350), Color.White, "АКЦИЯ")
    }

    Column(
        modifier = modifier
            .background(bg, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Black, color = fg)
        )

        Text(
            text = "${formatMoney(price)} ₽",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Black, color = fg)
        )

        // регулярную цену показываем всегда, но если совпадает — можно без зачёркивания
        val decoration =
            if (regularPrice != price) TextDecoration.LineThrough else TextDecoration.None

        Text(
            text = "рег.: ${formatMoney(regularPrice)} ₽",
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = fg,
                textDecoration = decoration
            )
        )
    }
}

private fun formatMoney(value: BigDecimal): String {
    val df = DecimalFormat("0.00")
    return df.format(value)
}
