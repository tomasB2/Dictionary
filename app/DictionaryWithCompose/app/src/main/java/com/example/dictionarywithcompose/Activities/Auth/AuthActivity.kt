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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.R
import com.example.dictionarywithcompose.Utils.getTextFromResourceId

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeConstructor {
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
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize().background(
            Color.White,
        ),
        contentAlignment = Alignment.Center,
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
            handleEvent(AuthenticationEvent.Authenticate(context))
        } else {
            AuthForm(
                Modifier.fillMaxSize(),
                authenticationState.email,
                authenticationState.password,
                authenticationState.username,
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
                onUsernameChanged = { username ->
                    handleEvent(
                        AuthenticationEvent.UsernameChanged(username),
                    )
                },
                onAuthenticate = {
                    handleEvent(AuthenticationEvent.Loading)
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
    username: String?,
    authenticationMode: AuthenticationMode,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onToggleAuthenticationMode: () -> Unit,
    onUsernameChanged: (username: String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
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
                    if (authenticationMode == AuthenticationMode.SIGN_UP) {
                        EmailInput(
                            modifier = Modifier.fillMaxWidth(),
                            email = email ?: "",
                            onEmailChanged = onEmailChanged,
                        )
                    }
                    UsernameInput(
                        modifier = Modifier.fillMaxWidth(),
                        username = username ?:"",
                        onUsernameChanged = onUsernameChanged,
                    )
                    PasswordInput(
                        modifier = Modifier.fillMaxWidth(),
                        password = password ?: "",
                        onPasswordChanged = onPasswordChanged,
                    )
                }
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                    TogglesState(
                        authenticationMode = authenticationMode,
                        onClicked = {
                            onAuthenticate()
                        },
                    )
                }
            }
        }
        Box(Modifier.align(Alignment.BottomCenter)) {
            StateDisplayedBottom(authenticationState = authenticationMode) {
                onToggleAuthenticationMode()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameInput(modifier: Modifier, username: String, onUsernameChanged: (username: String) -> Unit) {
    TextField(
        modifier = modifier,
        value = username,
        onValueChange = { username ->
            onUsernameChanged(username)
        },
        label = {
            Text(
                text = stringResource(R.string.label_Username),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
    )
}

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
        color = Color.Black,
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
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
            ToggleStateContent(resourceId = R.string.cd_login) {
                onClicked()
            }
        } else {
            ToggleStateContent(resourceId = R.string.cd_register) {
                onClicked()
            }
        }
    }
}

@Composable
fun ToggleStateContent(
    resourceId: Int,
    onClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = { onClicked() }) {
            Text(text = getTextFromResourceId(context = LocalContext.current, resourceId))
        }
    }
}

@Composable
fun ChangeState(
    primaryText: String,
    resourceId: Int,
    onClicked: () -> Unit,
) {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = primaryText)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = getTextFromResourceId(LocalContext.current, resourceId),
                modifier = Modifier.clickable {
                    onClicked()
                },
                style = TextStyle(
                    color = Color.Blue,
                ),
            )
        }
    }
}

@Composable
fun StateDisplayedBottom(
    authenticationState: AuthenticationMode,
    onClicked: () -> Unit,
) {
    if (authenticationState == AuthenticationMode.SIGN_IN) {
        ChangeState(primaryText = "Don't have an account?", resourceId = R.string.cd_register) {
            onClicked()
        }
    } else {
        ChangeState(primaryText = "Already have an account?", resourceId = R.string.cd_login) {
            onClicked()
        }
    }
}

