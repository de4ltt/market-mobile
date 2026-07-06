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
        menuCategory.apply {
            Image(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight(0.9f)
                    .align(iconAlignment),
                painter = painterResource(icon),
                contentDescription = title,
                colorFilter = if (!enabled)
                    ColorFilter.colorMatrix(grayscaleMatrix)
                else
                    null
            )

            Text(
                modifier = Modifier
                    .padding(15.dp)
                    .align(textAlignment),
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Colors.WHITE else Colors.LIGHT_GRAY
            )
        }
    }
}
