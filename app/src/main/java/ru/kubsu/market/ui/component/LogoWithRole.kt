package ru.kubsu.market.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.R
import ru.kubsu.market.model.Role
import ru.kubsu.market.ui.theme.Colors

@Composable
fun LogoWithRole(
    role: Role?
) = Box(
    modifier = Modifier
        .padding(vertical = 30.dp)
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Image(
        painter = painterResource(R.drawable.logo),
        modifier = Modifier
            .align(Alignment.CenterStart)
            .height(60.dp),
        contentDescription = "logo"
    )
    role?.let {
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = Colors.GRAY
                    )
                ) {
                    append("Ваша роль: ")
                }

                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Colors.WHITE
                    )
                ) {
                    append(role.title)
                }

            },
            fontSize = 13.sp,
        )
    }
}