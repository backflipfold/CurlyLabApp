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

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    return emailRegex.matches(email)
}

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<String?>(null) }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }
    val registerState by viewModel.registerState.collectAsState()

    fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is HttpException -> {
                when (error.code()) {
                    400 -> "Некорректные данные"
                    409 -> "Пользователь с таким email уже существует"
                    else -> "Ошибка сервера (${error.code()})"
                }
            }

            is IOException -> "Проблемы с интернет-соединением"
            else -> "Ошибка регистрации: ${error.localizedMessage ?: "неизвестная ошибка"}"
        }
    }

    LaunchedEffect(registerState) {
        registerState?.onSuccess {
            onSignUpSuccess()
        }
    }

    val errorMessage = remember {
        derivedStateOf {
            passwordError.value ?: registerState?.exceptionOrNull()?.let { getErrorMessage(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBeige)
            .verticalScroll(rememberScrollState())
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
                    text = "Регистрация",
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
                            passwordError.value = null
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
                        value = username.value,
                        onValueChange = {
                            username.value = it
                            passwordError.value = null
                        },
                        label = { Text("Имя", color = DarkGreen.copy(alpha = 0.6f)) },
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
                            passwordError.value = null
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

                    OutlinedTextField(
                        value = confirmPassword.value,
                        onValueChange = {
                            confirmPassword.value = it
                            passwordError.value = null
                        },
                        label = {
                            Text(
                                "Подтвердите пароль",
                                color = DarkGreen.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible.value = !confirmPasswordVisible.value
                            }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible.value) ImageVector.vectorResource(
                                        id = R.drawable.visibility_on
                                    ) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                                    contentDescription = if (confirmPasswordVisible.value) "Скрыть пароль" else "Показать пароль",
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
                                password.value.isEmpty() || confirmPassword.value.isEmpty() || username.value.isEmpty() || email.value.isEmpty() -> {
                                    passwordError.value = "Заполните все поля"
                                }

                                !isValidEmail(email.value) -> {
                                    passwordError.value = "Введите корректный email"
                                }

                                password.value != confirmPassword.value -> {
                                    passwordError.value = "Пароли не совпадают"
                                }

                                password.value.length < 6 || password.value.length > 20 -> {
                                    passwordError.value =
                                        "Пароль должен содержать 6-20 символов"
                                }

                                else -> {
                                    viewModel.register(username.value, email.value, password.value)
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
                            text = "Зарегистрироваться",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (errorMessage.value != null) {
                        Text(
                            text = errorMessage.value!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Уже есть аккаунт? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGreen.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSignIn() }
                    )
                }
            }
        }
    }
}