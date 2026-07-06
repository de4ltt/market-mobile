package ru.kubsu.market.core.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import java.math.BigDecimal

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return when (val input = decoder as? JsonDecoder) {
            null -> BigDecimal(decoder.decodeString())
            else -> {
                val element = input.decodeJsonElement()
                when (element) {
                    is JsonPrimitive -> {
                        if (element.isString) BigDecimal(element.content)
                        else BigDecimal(element.double.toString())
                    }
                    else -> throw SerializationException("Expected JsonPrimitive for BigDecimal")
                }
            }
        }
    }
}
