package ru.kubsu.market.core.model

enum class MenuCategory(
    val title: String
) {
    PRODUCTS(title = "Товары"),
    STORAGE(title = "Склад/зал"),
    RECEIVAL(title = "Приёмка"),
    REPORT(title = "Отчёты"),
    EMPLOYEES(title = "Сотрудники"),
    MY_SHIFT(title = "Личный\nкабинет"),
    DICTIONARIES(title = "Другие\nсправочники");

    companion object {
        fun filterByRole(role: Role?): List<MenuCategory> =
            if (role == null)
                emptyList()
            else
                when (role) {
                    Role.DIRECTOR -> MenuCategory.entries.toList()
                    Role.FIRED -> emptyList()
                    Role.SELLER -> listOf(PRODUCTS, MY_SHIFT)
                    Role.COMMODITY_EXPERT -> listOf(PRODUCTS, MY_SHIFT, DICTIONARIES, STORAGE)
                    Role.STOREKEEPER -> listOf(MY_SHIFT, STORAGE, RECEIVAL)
                }
    }
}
