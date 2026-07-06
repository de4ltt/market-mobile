package ru.kubsu.market.core.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromBigDecimal(value: String?): java.math.BigDecimal? {
        return value?.let { java.math.BigDecimal(it) }
    }

    @TypeConverter
    fun bigDecimalToString(value: java.math.BigDecimal?): String? {
        return value?.toString()
    }
}
