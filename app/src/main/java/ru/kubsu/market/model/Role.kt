package ru.kubsu.market.model

import android.util.Log

enum class Role(val title: String) {
    DIRECTOR(title = "Директор"),
    FIRED(title = "Уволен"),
    SELLER(title = "Продавец"),
    COMMODITY_EXPERT(title = "Товаровед"),
    STOREKEEPER(title = "Кладовщик");

    companion object {
        fun findByRole(role: String) = Role.entries.firstOrNull {
            val roleClear = role.split("_")[1]
            Log.d("HAEEOR", "${it.name} $roleClear")
            it.name.contains(other = roleClear, ignoreCase = true)
        }
    }
}