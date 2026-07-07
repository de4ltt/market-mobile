package ru.kubsu.market.feature.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.model.MenuCategory
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.core.ui.theme.Colors
import java.time.LocalTime

@Composable
fun MainMenuScreen(
    role: Role?,
    isCheckedIn: Boolean,
    orderFormed: Boolean,
    onMenuCategorySelected: (MenuCategory) -> Unit,
    onCheckInOut: (isCheckedIn: Boolean) -> Unit,
    onFormOrder: () -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {

    val categories = MenuCategory.filterByRole(role)

    if (categories.isNotEmpty())
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Выберите категорию",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = Colors.WHITE
            )

            val spacing = 10.dp
            val cardHeight = with(LocalDensity.current) {
                LocalWindowInfo.current.containerSize.height.toDp()
            }.times(0.13f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                BoxMenuCategoryCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight),
                    enabled = isCheckedIn,
                    menuCategory = categories.first(),
                    onClick = { onMenuCategorySelected(categories.first()) }
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    items(categories.slice(1..categories.lastIndex)) {
                        BoxMenuCategoryCard(
                            modifier = Modifier.height(cardHeight),
                            menuCategory = it,
                            enabled = isCheckedIn,
                            onClick = { onMenuCategorySelected(it) }
                        )
                    }
                }
            }
        }

    Column(
        modifier = Modifier.align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val checkButtonText = if (isCheckedIn) "Закончить смену" else "Начать смену"
        val type = if (isCheckedIn) AppButtonType.NEGATIVE else AppButtonType.POSITIVE

        AppButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = checkButtonText,
            buttonType = type,
            onClick = { onCheckInOut(isCheckedIn) }
        )

        if (role == Role.DIRECTOR)
            AppButton(
                onClick = onFormOrder,
                enabled = !orderFormed && LocalTime.now() in LocalTime.of(8, 0)..LocalTime.of(9, 0),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Colors.WHITE
                        )
                    ) {
                        append(if (orderFormed) "Приказ успешно выполнен" else "Запустить приказ")
                    }

                    if (!orderFormed)
                        withStyle(
                            SpanStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = Colors.LIGHT_GRAY
                            )
                        ) {
                            append("\n" + "(доступно с 8:00 по 9:00)")
                        }
                },
                buttonType = AppButtonType.POSITIVE
            )
    }
}

private val MenuCategory.icon: Int
    get() = when (this) {
        MenuCategory.PRODUCTS -> ru.kubsu.market.core.ui.R.drawable.products_box
        MenuCategory.STORAGE -> ru.kubsu.market.core.ui.R.drawable.storage
        MenuCategory.RECEIVAL -> ru.kubsu.market.core.ui.R.drawable.receival
        MenuCategory.REPORT -> ru.kubsu.market.core.ui.R.drawable.accounting
        MenuCategory.EMPLOYEES -> ru.kubsu.market.core.ui.R.drawable.employees
        MenuCategory.MY_SHIFT -> ru.kubsu.market.core.ui.R.drawable.my_shift
        MenuCategory.DICTIONARIES -> ru.kubsu.market.core.ui.R.drawable.prices
    }

private val MenuCategory.iconAlignment: Alignment
    get() = when (this) {
        MenuCategory.PRODUCTS -> Alignment.CenterEnd
        MenuCategory.STORAGE -> Alignment.BottomEnd
        MenuCategory.RECEIVAL -> Alignment.CenterStart
        MenuCategory.REPORT -> Alignment.BottomStart
        MenuCategory.EMPLOYEES -> Alignment.CenterEnd
        MenuCategory.MY_SHIFT -> Alignment.CenterStart
        MenuCategory.DICTIONARIES -> Alignment.CenterEnd
    }

private val MenuCategory.textAlignment: Alignment
    get() = when (this) {
        MenuCategory.PRODUCTS -> Alignment.BottomStart
        MenuCategory.STORAGE -> Alignment.TopStart
        MenuCategory.RECEIVAL -> Alignment.TopEnd
        MenuCategory.REPORT -> Alignment.BottomEnd
        MenuCategory.EMPLOYEES -> Alignment.TopStart
        MenuCategory.MY_SHIFT -> Alignment.BottomEnd
        MenuCategory.DICTIONARIES -> Alignment.BottomStart
    }

private val MenuCategory.paddingValues: PaddingValues
    get() = when (this) {
        MenuCategory.PRODUCTS -> PaddingValues(end = 20.dp)
        MenuCategory.RECEIVAL -> PaddingValues(start = 10.dp)
        MenuCategory.REPORT -> PaddingValues(start = 10.dp)
        MenuCategory.EMPLOYEES -> PaddingValues(end = 10.dp)
        MenuCategory.MY_SHIFT -> PaddingValues(start = 10.dp)
        MenuCategory.DICTIONARIES -> PaddingValues(end = 10.dp)
        else -> PaddingValues(0.dp)
    }

@Composable
private fun BoxMenuCategoryCard(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    menuCategory: MenuCategory,
    onClick: () -> Unit = {}
) {

    val grayscaleMatrix = remember {
        ColorMatrix().apply { setToSaturation(0f) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .clickable(
                enabled = enabled,
                onClick = { if (enabled) onClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Image(
            modifier = Modifier
                .padding(menuCategory.paddingValues)
                .fillMaxHeight(0.9f)
                .align(menuCategory.iconAlignment),
            painter = painterResource(menuCategory.icon),
            contentDescription = menuCategory.title,
            colorFilter = if (!enabled)
                ColorFilter.colorMatrix(grayscaleMatrix)
            else
                null
        )

        Text(
            modifier = Modifier
                .padding(15.dp)
                .align(menuCategory.textAlignment),
            text = menuCategory.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Colors.WHITE else Colors.LIGHT_GRAY
        )
    }
}
