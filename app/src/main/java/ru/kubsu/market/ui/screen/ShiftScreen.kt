package ru.kubsu.market.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.R
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.component.AppButtonType
import ru.kubsu.market.ui.component.VacationEditDialog
import ru.kubsu.market.ui.cringe.ScreenEvent
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun ShiftScreen(
    employee: Employee,
    role: Role,
    hours: Int,
    underwork: Int,
    overwork: Int,
    vacation: Vacation?,
    onEvent: (ScreenEvent) -> Unit
) {

    var isVacationDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (isVacationDialogOpen)
        VacationEditDialog(
            onDismiss = { isVacationDialogOpen = false },
            onConfirm = {
                onEvent(ScreenEvent.OnVacationRequested(it))
                isVacationDialogOpen = false
            }
        )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = "Личный кабинет",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = Colors.WHITE
            )

            Icon(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .scale(1.5f)
                    .clickable(
                        onClick = { onEvent(ScreenEvent.OnLogOut) },
                        indication = null,
                        interactionSource = null
                    ),
                tint = Colors.DARK_BLUE
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                TitledBlock(title = "Персональная информация") {
                    InfoBlock(
                        info = mapOf(
                            "ID" to "${employee.employeeId}",
                            "ФИО" to employee.fullName,
                            "Родился" to "${employee.birthDate}"
                        )
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    TitledBlock(
                        modifier = Modifier.weight(1f),
                        title = "Паспорт"
                    ) {
                        IconWithTextBlock(
                            modifier = Modifier.aspectRatio(1f),
                            iconRes = R.drawable.passport,
                            text = mapOf(
                                "Серия" to employee.passportSeries,
                                "Номер" to employee.passportNumber
                            )
                        )
                    }

                    TitledBlock(
                        modifier = Modifier.weight(1f),
                        title = "Должность"
                    ) {
                        IconWithTextBlock(
                            modifier = Modifier.aspectRatio(1f),
                            iconRes = R.drawable.tractor,
                            text = mapOf(
                                "Роль" to role.title,
                                "Отделение" to employee.department
                            )
                        )
                    }
                }
            }

            item {
                TitledBlock(title = "Отработанные часы") {
                    InfoBlock(
                        info = mapOf(
                            "Часы" to "${hours - underwork} / $hours",
                            "Недоработка" to "$underwork",
                            "Переработка" to "$overwork"
                        )
                    )
                }
            }

            item {
                TitledBlock(title = "Контактная информация") {
                    InfoBlock(
                        info = mapOf(
                            "Телефон" to employee.personalPhone,
                            "Раб. телефон" to employee.workPhone,
                            "Почта" to employee.email
                        )
                    )
                }
            }

            item {
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Отчёты",
                    buttonType = AppButtonType.DEFAULT,
                    onClick = {
                        onEvent(ScreenEvent.OnReportsRequestedForEmployee(employeeId = employee.employeeId!!))
                    }
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val vacationText = if (vacation != null) {
                        if (vacation.reviewed) {
                            if (vacation.approved)
                                "Отпуск одобрен"
                            else
                                "Запросить отпуск"
                        } else
                            "Отпуск ожидает рассмотрения"
                    } else "Запросить отпуск"
                    val vacationSubtitle = if (vacation != null) {
                        if (vacation.reviewed) {
                            if (vacation.approved)
                                "(С ${vacation.startDate} по ${vacation.endDate})"
                            else
                                "Прошлый запрос не был одобрен"
                        } else
                            "(С ${vacation.startDate} по ${vacation.endDate})"
                    } else null

                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Colors.WHITE
                                )
                            ) {
                                append(vacationText)
                            }

                            vacationSubtitle?.let {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Colors.LIGHT_GRAY
                                    )
                                ) {
                                    append("\n" + vacationSubtitle)
                                }
                            }
                        },
                        buttonType = AppButtonType.POSITIVE,
                        onClick = {
                            if (vacation == null || vacation.reviewed && !vacation.approved)
                                isVacationDialogOpen = true
                        }
                    )

                    AppButton(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .fillMaxWidth(),
                        text = "Уволиться",
                        buttonType = AppButtonType.NEGATIVE,
                        onClick = {
                            Toast.makeText(context, "Вы обречены. Смиритесь", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun IconWithTextBlock(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: Map<String, String>
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(12.dp))
        .background(Colors.DARK_GRAY)
        .padding(15.dp)
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = "",
        modifier = Modifier
            .align(Alignment.TopStart)
            .fillMaxWidth(0.2f),
        tint = Colors.LIGHT_GRAY
    )

    Column(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        text.entries.forEachIndexed { index, entry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.key,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Colors.WHITE
                )

                Text(
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                    text = entry.value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.LIGHT_GRAY
                )
            }
        }
    }
}

@Composable
private fun TitledBlock(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(10.dp)
) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = Colors.LIGHT_GRAY
    )
    content()
}

@Composable
private fun InfoBlock(
    modifier: Modifier = Modifier,
    info: Map<String, String>
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(12.dp))
        .background(Colors.DARK_GRAY)
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        info.entries.forEachIndexed { index, entry ->

            if (index != 0)
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Colors.LIGHT_GRAY,
                    thickness = 0.5.dp
                )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.key,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Colors.WHITE
                )

                Text(
                    text = entry.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.LIGHT_GRAY
                )
            }
        }
    }
}