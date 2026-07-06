package ru.kubsu.market.ui.component


import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.kubsu.market.model.Product
import ru.kubsu.market.model.ProductPrice
import ru.kubsu.market.model.pricing.PriceFormationResult
import ru.kubsu.market.ui.theme.Colors

@Composable
fun ProductRepresentationCard(
    product: Product,
    priceInfo: ProductPrice?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("PRICE", "$priceInfo")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .clickable { isExpanded = !isExpanded }
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AnimatedContent(targetState = isExpanded, label = "expand_product") { expanded ->
                if (expanded) {
                    // существующий FullContent товара (barcode + поля)
                    product.FullContent()
                } else {
                    // существующий ShortContent товара
                    product.ShortContent()
                }
            }

            if (priceInfo != null) {
                PriceTagChip(
                    price = priceInfo.currentPrice,
                    regularPrice = priceInfo.regularPrice,
                    type = PriceFormationResult.PriceTagType.entries.find {
                        it.name.equals(
                            priceInfo.labelType,
                            true
                        )
                    } ?: PriceFormationResult.PriceTagType.WHITE
                )
            }
        }
    }
}
