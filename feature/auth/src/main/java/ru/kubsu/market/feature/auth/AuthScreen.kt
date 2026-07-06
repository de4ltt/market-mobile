package ru.kubsu.market.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kubsu.market.core.ui.R
import ru.kubsu.market.core.ui.component.AppButton
import ru.kubsu.market.core.ui.theme.Colors

@Composable
fun AuthScreen(
    onLogin: (String, String) -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(color = Colors.BLACK),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Icon(
        painter = painterResource(R.drawable.logo_large),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxHeight(0.5f)
            .scale(1.2f)
            .offset(y = (-50).dp)
    )

    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Добро пожаловать",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.WHITE
            )

            Text(
                text = "Пожалуйста, войдите в аккаунт",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Colors.GRAY
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Colors.DARK_GRAY)
                    .padding(15.dp),
                value = login,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.GRAY
                ),
                singleLine = true,
                onValueChange = { login = it }
            ) { innerTextField ->
                Box {
                    if (login.isEmpty())
                        Text(
                            text = "Логин",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Colors.GRAY
                        )
                    else innerTextField()
                }
            }

            BasicTextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Colors.DARK_GRAY)
                    .padding(15.dp),
                value = password,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Colors.GRAY
                ),
                singleLine = true,
                onValueChange = { password = it }
            ) { innerTextField ->
                Box {
                    if (password.isEmpty())
                        Text(
                            text = "Пароль",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Colors.GRAY
                        )
                    else innerTextField()
                }
            }
        }

        AppButton(
            enabled = login.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.85f),
            onClick = { onLogin(login, password) },
            text = "Войти"
        )
    }
}
