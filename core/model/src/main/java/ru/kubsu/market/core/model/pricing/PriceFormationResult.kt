package ru.kubsu.market.core.model.pricing

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class PriceFormationResult(
    val formedAt: String? = null,
    val productPrices: List<ProductPrice> = emptyList(),
    val couponsToPrint: List<CouponToPrint> = emptyList(),
    val priceTagsToPrint: List<PriceTagToPrint> = emptyList(),
    val stopList: List<StopListItem> = emptyList(),
    val warnings: List<String> = emptyList()
) {
    @Serializable
    data class ProductPrice(
        val productId: Int,
        @Serializable(with = BigDecimalSerializer::class)
        val regularPrice: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val appliedPrice: BigDecimal,
        val priceTagType: PriceTagType,
        val source: PriceSource? = null,
        val discountPercent: Int? = null
    )

    @Serializable
    data class PriceSource(
        val kind: PriceListKind,
        val priceListId: Int? = null,
        val comment: String? = null
    )

    @Serializable
    enum class PriceListKind {
        REGULAR,
        MARKDOWN,
        PROMO
    }

    @Serializable
    enum class PriceTagType {
        WHITE,
        YELLOW,
        ACTION
    }

    @Serializable
    data class PriceTagToPrint(
        val productId: Int,
        val priceTagType: PriceTagType,
        @Serializable(with = BigDecimalSerializer::class)
        val regularPrice: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val appliedPrice: BigDecimal
    )

    @Serializable
    data class CouponToPrint(
        val couponId: String? = null,
        val productId: Int,
        val receivedProductId: Int? = null,
        @Serializable(LocalDateSerializer::class)
        val expirationDate: LocalDate,
        val discountType: DiscountType,
        @Serializable(with = BigDecimalSerializer::class)
        val discountValue: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val finalPrice: BigDecimal
    )

    @Serializable
    enum class DiscountType {
        PERCENT,
        AMOUNT
    }

    @Serializable
    data class StopListItem(
        val productId: Int,
        val reason: StopReason,
        @Serializable(with = BigDecimalSerializer::class)
        val proposedPrice: BigDecimal? = null,
        val limits: Limits? = null,
        val comment: String? = null
    )

    @Serializable
    data class Limits(
        val dailyChangeMaxPercent: Int? = null,
        val maxMarkupPercent: Int? = null
    )

    @Serializable
    enum class StopReason {
        DAILY_CHANGE_LIMIT_EXCEEDED,
        MARKUP_CAP_EXCEEDED,
        INVALID_INPUT_PRICE,
        OTHER
    }
}

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }
}
