package ru.kubsu.market.core.model

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.kubsu.market.core.ui.R as CoreUiR
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ItemsRepresentationScreen(
    items: List<IItemRepresentable>,
    className: String
) = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp)
) {

    Text(
        text = className,
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
        color = Colors.WHITE
    )

    Crossfade(
        modifier = Modifier.weight(1f),
        targetState = items.isNotEmpty()
    ) {
        if (it) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) { items(items) { item -> ItemRepresentationCard(item) } }
        } else {
            Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
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

@Composable
fun ItemsRepresentationScreen(
    items: List<IItemRepresentable>,
    className: String? = null,
    container: @Composable (IItemRepresentable) -> Unit = { item -> ItemRepresentationCard(item = item) },
    onClick: (IItemRepresentable) -> Unit = {},
    addDialog: (@Composable (onDismiss: () -> Unit) -> Unit)? = null,
    onDelete: ((IItemRepresentable) -> Unit)? = null,
    buttonText: String,
    onButtonClick: () -> Unit,
    buttonEnabled: Boolean = true
) {

    var isDialogOpened by remember { mutableStateOf(false) }

    if (isDialogOpened)
        addDialog?.let {
            it(
                { isDialogOpened = false },
            )
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            className?.let {
                Text(
                    text = className,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    color = Colors.WHITE
                )
            }

            Crossfade(
                modifier = Modifier.weight(1f),
                targetState = items.isNotEmpty()
            ) {
                if (it) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
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
                    Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
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
                    contentDescription = "delete_icon"
                )
            }
        }

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


@Composable
fun ItemsRepresentationScreen(
    items: List<IItemRepresentable>,
    className: String? = null,
    container: @Composable (IItemRepresentable) -> Unit = { item -> ItemRepresentationCard(item = item) },
    onClick: (IItemRepresentable) -> Unit = {},
    addDialog: (@Composable (onDismiss: () -> Unit) -> Unit)? = null,
    onDelete: ((IItemRepresentable) -> Unit)? = null
) {

    var isDialogOpened by remember { mutableStateOf(false) }

    if (isDialogOpened)
        addDialog?.let {
            it(
                { isDialogOpened = false },
            )
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            className?.let {
                Text(
                    text = className,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    color = Colors.WHITE
                )
            }

            Crossfade(
                modifier = Modifier.weight(1f),
                targetState = items.isNotEmpty()
            ) {
                if (it) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
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
                    Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
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
                    contentDescription = "delete_icon"
                )
            }
        }
    }
}


@Composable
fun ItemRepresentationCardExpanded(
    item: IItemRepresentable,
    onDelete: (() -> Unit)? = null,
    onClick: (IItemRepresentable) -> Unit = {},
) = Box(
    modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(color = Colors.DARK_GRAY)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(20.dp)
        .clickable(
            onClick = { onClick(item) },
            indication = null,
            interactionSource = null
        )
) {
    item.FullContent()

    onDelete?.let {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = onDelete,
                    indication = null,
                    interactionSource = null
                ),
            painter = painterResource(CoreUiR.drawable.bin),
            tint = Colors.RED,
            contentDescription = "delete_icon"
        )
    }
}

@Composable
fun ItemRepresentationCard(
    item: IItemRepresentable
) {

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Colors.DARK_GRAY)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
            .clickable(
                onClick = { isExpanded = !isExpanded },
                indication = null,
                interactionSource = null
            )
    ) {
        AnimatedContent(
            targetState = isExpanded,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Top)
        ) { if (it) item.FullContent() else item.ShortContent() }
    }
}
