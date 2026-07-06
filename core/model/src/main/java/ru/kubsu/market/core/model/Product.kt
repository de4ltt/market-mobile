package ru.kubsu.market.core.model

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import kotlinx.serialization.Serializable
import ru.kubsu.market.core.ui.theme.Colors
import java.time.LocalDate
import kotlin.random.Random

@Serializable
data class Product(
    val productId: Int? = null,
    val name: String,
    val manufacturerName: String,
    val manufacturerCountry: String,
    val manufacturerCode: String,
    val size: String,
    val unit: String,
    val barcode: String? = null,
    val additionalInfo: String? = null,
    val storageRequirement: String? = "regular"
): IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$productId | $name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    val expirationDays: Long = Random.nextLong(60)

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "Производитель" to manufacturerName,
            "Код производителя" to manufacturerCode,
            "Страна" to manufacturerCountry,
            "Годен до" to LocalDate.now().plusDays(expirationDays).toString(),
            "Размеры" to size,
            "Единица измерения" to unit,
            "Технический срок годности" to expirationDays.toString()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            barcode?.let {
                BarCode(code = it)
            }
            FieldsRepresentation(
                map = map
            )
        }
    }

    @Composable
    private fun BarCode(
        code: String,
        modifier: Modifier = Modifier
    ) {
        val url = remember(code) {
            "https://www.scandit.com/api/generate-barcode/?type=code128&value=$code"
        }

        Box(
            modifier = modifier
                .background(Colors.WHITE, RoundedCornerShape(12.dp))
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = "Barcode",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 3.dp,
                            color = Colors.DARK_BLUE
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Не удалось загрузить штрихкод",
                            color = Colors.DARK_GRAY
                        )
                    }
                }
            )
        }
    }

    companion object {
        val className = "Продукты"
    }
}
