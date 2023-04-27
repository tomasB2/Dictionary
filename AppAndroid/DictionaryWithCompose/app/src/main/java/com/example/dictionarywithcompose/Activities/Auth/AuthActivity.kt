package com.example.dictionarywithcompose.Activities.Auth // ktlint-disable package-name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthState
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationEvent
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationMode
import com.example.dictionarywithcompose.R
import com.example.dictionarywithcompose.ui.theme.DarkPurpleBlue
import com.example.dictionarywithcompose.ui.theme.DictionaryWithComposeTheme
import com.example.dictionarywithcompose.ui.theme.OceanBlue
import com.example.dictionarywithcompose.ui.theme.Purple20

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryWithComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AuthComposable()
                }
            }
        }
    }
}

@Composable
fun AuthComposable() {
    val viewModel: AuthViewModel = viewModel()
    MaterialTheme {
        AuthContents(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = viewModel.uiState.collectAsState().value,
            handleEvent = viewModel::handleEvent,
        )
    }
}

@Composable
fun AuthContents(
    modifier: Modifier = Modifier,
    authenticationState: AuthState,
    handleEvent: (event: AuthenticationEvent) -> Unit,
) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to Purple20,
                    0.35f to OceanBlue,
                    1f to DarkPurpleBlue,
                ),
            ),
        ),
        contentAlignment = Alignment.Center,
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthForm(
                Modifier.fillMaxSize(),
                authenticationState.email,
                authenticationState.password,
                authenticationState.authenticationMode,
                onEmailChanged = { email ->
                    handleEvent(
                        AuthenticationEvent.EmailChanged(email),
                    )
                },
                onPasswordChanged = { password ->
                    handleEvent(
                        AuthenticationEvent.PasswordChanged(password),
                    )
                },
                onAuthenticate = {
                    handleEvent(AuthenticationEvent.Authenticate)
                },
                onToggleAuthenticationMode = {
                    handleEvent(
                        AuthenticationEvent.ToggleAuthenticationMode,
                    )
                },
            )
        }
    }
}

@Composable
fun AuthForm(
    modifier: Modifier = Modifier,
    email: String?,
    password: String?,
    authenticationMode: AuthenticationMode,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onToggleAuthenticationMode: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AuthTitle(
            modifier,
            authenticationMode = authenticationMode,
        )
        Spacer(modifier = Modifier.height(40.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment =
                Alignment.CenterHorizontally,
            ) {
                EmailInput(
                    modifier = Modifier.fillMaxWidth(),
                    email = email ?: "",
                    onEmailChanged = onEmailChanged,
                )
                PasswordInput(
                    modifier = Modifier.fillMaxWidth(),
                    password = password ?: "",
                    onPasswordChanged = onPasswordChanged,
                    onDoneClicked = onAuthenticate,
                )
            }
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                TogglesState(
                    authenticationMode = authenticationMode,
                    onClicked = {
                        onToggleAuthenticationMode()
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun AuthTitle(modifier: Modifier = Modifier.fillMaxWidth(), authenticationMode: AuthenticationMode) {
    Text(
        text = stringResource(
            when (authenticationMode) {
                AuthenticationMode.SIGN_IN -> {
                    R.string.title_Login
                }
                AuthenticationMode.SIGN_UP -> {
                    R.string.title_Register
                }
            },
        ),
        color = Color.White,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFF835597),
                    0.35f to Color(0xFFCAB9EB),
                    1f to Color(0xFF3D3D94),
                ),
            ),
        ),

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (email: String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = email,
        onValueChange = { email ->
            onEmailChanged(email)
        },
        label = {
            Text(
                text = stringResource(
                    id = R.string.label_email,
                ),
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChanged: (email: String) -> Unit,
    onDoneClicked: () -> Unit,
) {
    var isPasswordHidden by remember {
        mutableStateOf(true)
    }
    TextField(
        modifier = modifier,
        value = password,
        onValueChange = { password ->
            onPasswordChanged(password)
        },
        label = {
            Text(
                text = stringResource(
                    id = R.string.label_password,
                ),
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
            )
        },
        trailingIcon = {
            val icon = if (isPasswordHidden) {
                Icons.Default.VisibilityOff
            } else {
                Icons.Default.Visibility
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable {
                    isPasswordHidden = !isPasswordHidden
                },
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = if (isPasswordHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
    )
}

@Composable
fun TogglesState(authenticationMode: AuthenticationMode, onClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (authenticationMode == AuthenticationMode.SIGN_IN) {
            ToggleStateContent(primaryText = "Don't have an account?", resourceId = R.string.cd_register) {
                onClicked()
            }
        } else {
            ToggleStateContent(primaryText = "Already have an account?", resourceId = R.string.cd_login) {
                onClicked()
            }
        }
    }
}

@Composable
fun ToggleStateContent(
    primaryText: String,
    resourceId: Int,
    onClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = primaryText,
        )
        TextButton(
            onClick = {
                onClicked()
            },
        ) {
            Text(
                text = stringResource(
                    resourceId,
                ),
            )
        }
    }
}
