package ru.kubsu.market.core.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.model.UiDisplayable
import ru.kubsu.market.core.ui.R as CoreUiR
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ItemsRepresentationScreen(
    items: List<UiDisplayable>,
    className: String
) = ItemsRepresentationScreen(
    items = items,
    className = className,
    container = { item -> ItemRepresentationCard(item = item) }
)

@Composable
fun <T> ItemsRepresentationScreen(
    items: List<T>,
    className: String? = null,
    container: @Composable (T) -> Unit,
    onClick: (T) -> Unit = {},
    addDialog: (@Composable (onDismiss: () -> Unit) -> Unit)? = null,
    onDelete: ((T) -> Unit)? = null,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    buttonEnabled: Boolean = true
) {
    var isDialogOpened by remember { mutableStateOf(false) }

    if (isDialogOpened) {
        addDialog?.let { dialog ->
            dialog { isDialogOpened = false }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
/*            className?.let {
                Text(
                    text = it,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    color = Colors.WHITE
                )
            }*/

            Crossfade(
                modifier = Modifier.weight(1f),
                targetState = items.isNotEmpty()
            ) { hasItems ->
                if (hasItems) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        items(items) { item ->
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clickable(
                                        onClick = { onClick(item) },
                                        indication = null,
                                        interactionSource = null
                                    )
                            ) {
                                container(item)

                                onDelete?.let { deleteAction ->
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxWidth(0.15f)
                                            .align(Alignment.TopEnd)
                                            .clickable(
                                                onClick = { deleteAction(item) }
                                            ),
                                        painter = painterResource(CoreUiR.drawable.bin),
                                        tint = Colors.ORANGE,
                                        contentDescription = "delete_icon"
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Тут ничего нет",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = Colors.LIGHT_GRAY
                        )
                    }
                }
            }
        }

        addDialog?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .fillMaxWidth(0.15f)
                    .aspectRatio(1f)
                    .background(Colors.DARK_GRAY)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = { isDialogOpened = true }
                        ),
                    painter = painterResource(CoreUiR.drawable.plus),
                    tint = Colors.WHITE,
                    contentDescription = "add_icon"
                )
            }
        }

        if (buttonText != null && onButtonClick != null) {
            AppButton(
                enabled = buttonEnabled,
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
            )
        }
    }
}
