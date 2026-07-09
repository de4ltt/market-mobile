package ru.kubsu.market.core.model.pricing

import java.math.BigDecimal
import java.time.LocalDate

data class PriceFormationResult(
    val formedAt: String? = null,
    val productPrices: List<ProductPrice> = emptyList(),
    val couponsToPrint: List<CouponToPrint> = emptyList(),
    val priceTagsToPrint: List<PriceTagToPrint> = emptyList(),
    val stopList: List<StopListItem> = emptyList(),
    val warnings: List<String> = emptyList()
) {
    data class ProductPrice(
        val productId: Int,
        val regularPrice: BigDecimal,
        val appliedPrice: BigDecimal,
        val priceTagType: PriceTagType,
        val source: PriceSource? = null,
        val discountPercent: Int? = null
    )

    data class PriceSource(
        val kind: PriceListKind,
        val priceListId: Int? = null,
        val comment: String? = null
    )

    enum class PriceListKind {
        REGULAR,
        MARKDOWN,
        PROMO
    }

    enum class PriceTagType {
        WHITE,
        YELLOW,
        ACTION
    }

    data class PriceTagToPrint(
        val productId: Int,
        val priceTagType: PriceTagType,
        val regularPrice: BigDecimal,
        val appliedPrice: BigDecimal
    )

    data class CouponToPrint(
        val couponId: String? = null,
        val productId: Int,
        val receivedProductId: Int? = null,
        val expirationDate: LocalDate,
        val discountType: DiscountType,
        val discountValue: BigDecimal,
        val finalPrice: BigDecimal
    )

    enum class DiscountType {
        PERCENT,
        AMOUNT
    }

    data class StopListItem(
        val productId: Int,
        val reason: StopReason,
        val proposedPrice: BigDecimal? = null,
        val limits: Limits? = null,
        val comment: String? = null
    )

    data class Limits(
        val dailyChangeMaxPercent: Int? = null,
        val maxMarkupPercent: Int? = null
    )

    enum class StopReason {
        DAILY_CHANGE_LIMIT_EXCEEDED,
        MARKUP_CAP_EXCEEDED,
        INVALID_INPUT_PRICE,
        OTHER
    }
}
