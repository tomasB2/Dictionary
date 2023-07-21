package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import android.Manifest // ktlint-disable import-ordering
import android.annotation.SuppressLint // ktlint-disable import-ordering
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
// import com.example.dictionarywithcompose.Utils.checkCameraHardware
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0

class NavigationDrawer : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username")
        Log.v("AAAA", username ?: "no user")
        setContent {
            ThemeConstructor {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainNavigatorScreen(name = username ?: "User")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onNavigationIconClick: () -> Unit,
    // onMessageClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(
                onClick =
                onNavigationIconClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                )
            }
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainNavigatorScreen(name: String) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
        ),
    )
    fun updatePermitonState() {
        permissionsState.launchMultiplePermissionRequest()
    }
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(Modifier.fillMaxWidth()) {
                TopBar(
                    navController = navController,
                    onNavigationIconClick = {
                        scope.launch { scaffoldState.drawerState.open() }
                    },
                )
            }
        },
        bottomBar = { BottomBarNav(navController = navController, ::updatePermitonState, permissionsState) },
        drawerContent = {
            Log.v("AAAA", name)
            DrawerHeader(name)
            Spacer(modifier = Modifier.width(16.dp))
            DrawerBody(navController = navController, close = {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        },
        drawerBackgroundColor = MaterialTheme.colorScheme.background,
        drawerShape = MaterialTheme.shapes.large,
        drawerContentColor = MaterialTheme.colorScheme.background,
    ) {
        Row() {
            NavGraph(navHostController = navController, startDestination = NavRoute.Home, permissionsState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomBarNav(navController: NavHostController, updatePermition: KFunction0<Unit>, permition: MultiplePermissionsState) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "Home",
            onClick = { navController.navigate("Home") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                )
            },
            label = { Text("Home") },
        )
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "Dictionary",
            onClick = {
                if (permition.allPermissionsGranted) {
                    navController.navigate("Dictionary")
                } else {
                    updatePermition()
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Dictionary",
                )
            },
            label = { Text("Dictionary") },
        )
    }
}

@Composable
fun DrawerHeader(
    name: String = "Name",
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Column {
            Text(text = "Smart Dictionary\n", fontSize = 24.sp)
            Text("Welcome $name")
        }
    }
}

@Composable
private fun DrawerMenuItem(
    iconDrawableId: ImageVector,
    text: String,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = iconDrawableId,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}

@Composable
fun DrawerBody(navController: NavHostController, close: () -> Unit) {
    Column {
        DrawerMenuItem(
            iconDrawableId = Icons.Default.Home,
            text = NavRoute.Home,
            onItemClick = {
                navController.navigate(NavRoute.Home)
                close()
            },
        )
        DrawerMenuItem(
            iconDrawableId = Icons.Default.Search,
            text = NavRoute.Dictionary,
            onItemClick = {
                navController.navigate(NavRoute.Dictionary)
                close()
            },
        )
        DrawerMenuItem(
            iconDrawableId = Icons.Default.Camera,
            text = NavRoute.Translation,
            onItemClick = {
                navController.navigate(NavRoute.Translation)
                close()
            },
        )
        DrawerMenuItem(
            iconDrawableId = Icons.Default.Settings,
            text = NavRoute.Settings,
            onItemClick = {
                navController.navigate(NavRoute.Settings)
                close()
            },
        )
    }
}
