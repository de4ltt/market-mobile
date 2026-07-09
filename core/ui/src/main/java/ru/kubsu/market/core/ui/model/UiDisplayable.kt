package ru.kubsu.market.core.ui.model

interface UiDisplayable {
    val displayName: String
    val displayFields: Map<String, String>
    val barcode: String? get() = null
}
