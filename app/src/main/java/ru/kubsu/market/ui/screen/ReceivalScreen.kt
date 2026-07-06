package ru.kubsu.market.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.model.ReceivedProduct
import ru.kubsu.market.ui.component.AppButton
import ru.kubsu.market.ui.component.AppButtonType
import ru.kubsu.market.ui.cringe.ScreenEvent
import ru.kubsu.market.ui.theme.Colors

@Composable
fun ReceivalScreen(
    toResolveList: List<ReceivedProduct>,
    onEvent: (ScreenEvent) -> Unit
) {

    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    val toAccept = mutableListOf<ReceivedProduct>()
    val toRefuse = mutableListOf<ReceivedProduct>()

    var curIndex by remember {
        mutableStateOf(
            if (toResolveList.isNotEmpty()) 0 else null
        )
    }

    fun nextProduct() {
        if (curIndex == toResolveList.lastIndex)
            onEvent(
                ScreenEvent.OnProductsResolved(
                    acceptedProducts = toAccept,
                    refusedProducts = toRefuse
                )
            )
        else curIndex = curIndex?.plus(1)
    }

    var curProduct: ReceivedProduct? = null
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Приёмка товаров",
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            color = Colors.WHITE
        )

        curIndex?.let {

            curProduct = toResolveList[it]

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Colors.DARK_GRAY)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 25.dp, horizontal = 35.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                curProduct.product.FullContent()

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Принять",
                        onClick = {
                            toAccept.add(curProduct)
                            nextProduct()
                        }
                    )

                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Списать",
                        buttonType = AppButtonType.NEGATIVE,
                        onClick = {
                            isDialogVisible = true
                        }
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Тут ничего нет",
                fontSize = 19.sp,
                fontWeight = FontWeight.Normal,
                color = Colors.LIGHT_GRAY
            )
        }
    }

    CommentDialog(
        visible = isDialogVisible,
        onCancel = { isDialogVisible = false },
        onProceed = { string ->
            curProduct?.let {
                val withComment = curProduct.copy(comment = string)
                toRefuse.add(withComment)
                isDialogVisible = false
                nextProduct()
            }
        }
    )
}

@Composable
private fun CommentDialog(
    visible: Boolean,
    onCancel: () -> Unit,
    onProceed: (String) -> Unit
) {
    if (!visible) return

    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Причина списания")
        },
        text = {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Комментарий") },
                singleLine = false
            )
        },
        confirmButton = {
            TextButton(onClick = { onProceed(comment) }) {
                Text("Продолжить")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Отмена")
            }
        }
    )
}
