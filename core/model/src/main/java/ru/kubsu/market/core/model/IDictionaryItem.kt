package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer

interface IDictionaryItem {
    val endpoint: String
    val className: String
    val serializer: KSerializer<out IDictionaryItem>
}
