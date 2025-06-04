package aps.backflip.curlylab.presentation.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aps.backflip.curlylab.R
import aps.backflip.curlylab.presentation.auth.viewmodel.AuthViewModel
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.LightGreen
import retrofit2.HttpException
import java.io.IOException

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToResetPassword: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val loginError = remember { mutableStateOf<String?>(null) }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        loginState?.onSuccess {
            onSignInSuccess()
        }
    }

    fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is HttpException -> {
                when (error.code()) {
                    401 -> "Неверный email или пароль"
                    400 -> "Некорректные данные"
                    405 -> ""
                    else -> "Ошибка сервера (${error.code()})"
                }
            }

            is IOException -> "Проблемы с интернет-соединением"
            else -> "Ошибка входа: ${error.localizedMessage ?: "неизвестная ошибка"}"
        }
    }

    val errorMessage = remember {
        derivedStateOf {
            loginError.value ?: loginState?.exceptionOrNull()?.let { getErrorMessage(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBeige)
            .verticalScroll(rememberScrollState()),
        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.curly_wave),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Вход",
                    style = MaterialTheme.typography.displayLarge,
                    color = DarkGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            loginError.value = null
                        },
                        label = { Text("Email", color = DarkGreen.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkGreen,
                            unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                            focusedIndicatorColor = LightGreen,
                            unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                            loginError.value = null
                        },
                        label = { Text("Пароль", color = DarkGreen.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    imageVector = if (passwordVisible.value) ImageVector.vectorResource(
                                        id = R.drawable.visibility_on
                                    ) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                                    contentDescription = if (passwordVisible.value) "Скрыть пароль" else "Показать пароль",
                                    tint = DarkGreen.copy(alpha = 0.6f)
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkGreen,
                            unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                            focusedIndicatorColor = LightGreen,
                            unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = {
                            when {
                                email.value.isEmpty() || password.value.isEmpty() -> {
                                    loginError.value = "Заполните все поля"
                                }

                                password.value.length < 6 -> {
                                    loginError.value = "Пароль должен содержать минимум 6 символов"
                                }

                                else -> {
                                    // Чтобы миновать реальный вход
                                    //onSignInSuccess()
                                    // Реальный вход
                                    viewModel.login(email.value, password.value)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Войти",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (errorMessage.value != null) {
                        Text(
                            text = errorMessage.value!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Еще нет аккаунта? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGreen.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Зарегистрироваться",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSignUp() }
                    )
                }
            }
        }
    }
}