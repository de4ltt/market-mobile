package ru.kubsu.market.core.model

interface IItemRepresentable {
    val displayName: String
    val displayFields: Map<String, String>
    val barcode: String? get() = null
}
