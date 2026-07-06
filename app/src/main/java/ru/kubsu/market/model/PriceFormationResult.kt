package ru.kubsu.market.model.pricing

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.kubsu.market.ui.cringe.serializer.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Результат формирования "приказа" (формирования цен / купонов / очереди печати / стоп-листа).
 * ВАЖНО: Никакой локальной БД не требуется — этот объект можно хранить только в памяти ViewModel.
 */
@Serializable
data class PriceFormationResult(
    val formedAt: String? = null,

    /** Цены, которые нужно показывать и печатать как ценники */
    val productPrices: List<ProductPrice> = emptyList(),

    /** Купоны (обычно для конкретных поступлений с expirationDate) */
    val couponsToPrint: List<CouponToPrint> = emptyList(),

    /** Задания на печать ценников */
    val priceTagsToPrint: List<PriceTagToPrint> = emptyList(),

    /** Стоп-лист (что нельзя продавать / что нельзя менять) */
    val stopList: List<StopListItem> = emptyList(),

    /** Текстовые предупреждения/заметки (необязательно) */
    val warnings: List<String> = emptyList()
) {
    @Serializable
    data class ProductPrice(
        val productId: Int,

        /** Всегда указывать регулярную цену (по ТЗ) */
        @Serializable(with = BigDecimalSerializer::class)
        val regularPrice: BigDecimal,

        /** Цена, которую применяем (может совпадать с regularPrice) */
        @Serializable(with = BigDecimalSerializer::class)
        val appliedPrice: BigDecimal,

        /** Тип ценника: белый / жёлтый / акционный */
        val priceTagType: PriceTagType,

        /** Источник (какой прайс-лист победил) */
        val source: PriceSource? = null,

        /** Если надо — сколько % скидка относительно regularPrice */
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
        REGULAR,      // регулярный
        MARKDOWN,     // уценка
        PROMO         // промо/акция
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
        PERCENT,   // discountValue = 10 -> 10%
        AMOUNT     // discountValue = 25.00 -> минус 25 рублей
    }

    @Serializable
    data class StopListItem(
        val productId: Int,
        val reason: StopReason,

        /** Какая цена была рассчитана/предложена (если есть) */
        @Serializable(with = BigDecimalSerializer::class)
        val proposedPrice: BigDecimal? = null,

        /** Ограничения, которые сработали (если нужно показать/логировать) */
        val limits: Limits? = null,

        val comment: String? = null
    )

    @Serializable
    data class Limits(
        /** пример: дневной лимит изменения цены */
        val dailyChangeMaxPercent: Int? = null,
        /** пример: лимит наценки относительно входной цены */
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

/**
 * Сериализатор BigDecimal в JSON как строка:
 * "199.90"
 */
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
