package ru.kubsu.market.core.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import ru.kubsu.market.core.ui.R

enum class MenuCategory(
    val title: String,
    val icon: Int,
    val iconAlignment: Alignment,
    val textAlignment: Alignment,
    val paddingValues: PaddingValues = PaddingValues.Zero
) {
    PRODUCTS(
        title = "Товары",
        icon = R.drawable.products_box,
        iconAlignment = Alignment.CenterEnd,
        textAlignment = Alignment.BottomStart,
        paddingValues = PaddingValues(end = 20.dp)
    ),
    STORAGE(
        title = "Склад/зал",
        icon = R.drawable.storage,
        iconAlignment = Alignment.BottomEnd,
        textAlignment = Alignment.TopStart
    ),
    RECEIVAL(
        title = "Приёмка",
        icon = R.drawable.receival,
        iconAlignment = Alignment.CenterStart,
        textAlignment = Alignment.TopEnd,
        paddingValues = PaddingValues(start = 10.dp)
    ),
    REPORT(
        title = "Отчёты",
        icon = R.drawable.accounting,
        iconAlignment = Alignment.BottomStart,
        textAlignment = Alignment.BottomEnd,
        paddingValues = PaddingValues(start = 10.dp)
    ),
    EMPLOYEES(
        title = "Сотрудники",
        icon = R.drawable.employees,
        iconAlignment = Alignment.CenterEnd,
        textAlignment = Alignment.TopStart,
        paddingValues = PaddingValues(end = 10.dp)
    ),
    MY_SHIFT(
        title = "Личный\nкабинет",
        icon = R.drawable.my_shift,
        iconAlignment = Alignment.CenterStart,
        textAlignment = Alignment.BottomEnd,
        paddingValues = PaddingValues(start = 10.dp)
    ),
    DICTIONARIES(
        title = "Другие\nсправочники",
        icon = R.drawable.prices,
        iconAlignment = Alignment.CenterEnd,
        textAlignment = Alignment.BottomStart,
        paddingValues = PaddingValues(end = 10.dp)
    );

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
