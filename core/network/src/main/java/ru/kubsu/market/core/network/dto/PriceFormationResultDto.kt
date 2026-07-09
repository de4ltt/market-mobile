package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class PriceFormationResultDto(
    val formedAt: String? = null,
    val productPrices: List<ProductPriceDto> = emptyList(),
    val couponsToPrint: List<CouponToPrintDto> = emptyList(),
    val priceTagsToPrint: List<PriceTagToPrintDto> = emptyList(),
    val stopList: List<StopListItemDto> = emptyList(),
    val warnings: List<String> = emptyList()
) {
    @Serializable
    data class ProductPriceDto(
        val productId: Int,
        @Serializable(with = BigDecimalSerializer::class)
        val regularPrice: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val appliedPrice: BigDecimal,
        val priceTagType: PriceTagTypeDto,
        val source: PriceSourceDto? = null,
        val discountPercent: Int? = null
    )

    @Serializable
    data class PriceSourceDto(
        val kind: PriceListKindDto,
        val priceListId: Int? = null,
        val comment: String? = null
    )

    @Serializable
    enum class PriceListKindDto {
        REGULAR,
        MARKDOWN,
        PROMO
    }

    @Serializable
    enum class PriceTagTypeDto {
        WHITE,
        YELLOW,
        ACTION
    }

    @Serializable
    data class PriceTagToPrintDto(
        val productId: Int,
        val priceTagType: PriceTagTypeDto,
        @Serializable(with = BigDecimalSerializer::class)
        val regularPrice: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val appliedPrice: BigDecimal
    )

    @Serializable
    data class CouponToPrintDto(
        val couponId: String? = null,
        val productId: Int,
        val receivedProductId: Int? = null,
        @Serializable(LocalDateSerializer::class)
        val expirationDate: LocalDate,
        val discountType: DiscountTypeDto,
        @Serializable(with = BigDecimalSerializer::class)
        val discountValue: BigDecimal,
        @Serializable(with = BigDecimalSerializer::class)
        val finalPrice: BigDecimal
    )

    @Serializable
    enum class DiscountTypeDto {
        PERCENT,
        AMOUNT
    }

    @Serializable
    data class StopListItemDto(
        val productId: Int,
        val reason: StopReasonDto,
        @Serializable(with = BigDecimalSerializer::class)
        val proposedPrice: BigDecimal? = null,
        val limits: LimitsDto? = null,
        val comment: String? = null
    )

    @Serializable
    data class LimitsDto(
        val dailyChangeMaxPercent: Int? = null,
        val maxMarkupPercent: Int? = null
    )

    @Serializable
    enum class StopReasonDto {
        DAILY_CHANGE_LIMIT_EXCEEDED,
        MARKUP_CAP_EXCEEDED,
        INVALID_INPUT_PRICE,
        OTHER
    }
}
