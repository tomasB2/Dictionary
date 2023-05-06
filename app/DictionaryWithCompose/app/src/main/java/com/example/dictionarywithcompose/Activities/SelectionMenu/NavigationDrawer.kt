package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import android.Manifest
import android.annotation.SuppressLint // ktlint-disable import-ordering
import android.content.Context
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
// import com.example.dictionarywithcompose.Utils.checkCameraHardware
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0

class NavigationDrawer : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeConstructor {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainNavigatorScreen(LocalContext.current)
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
fun MainNavigatorScreen(context: Context) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    fun updatePermitonState() {
        permissionState.launchPermissionRequest()
    }
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navController = navController,
                onNavigationIconClick = {
                    scope.launch { scaffoldState.drawerState.open() }
                },
            )
        },
        bottomBar = { BottomBarNav(navController = navController, ::updatePermitonState, permissionState) },
        drawerContent = {
            DrawerHeader()
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
            NavGraph(navHostController = navController, startDestination = "home", permissionState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomBarNav(navController: NavHostController, updatePermition: KFunction0<Unit>, permition: PermissionState) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                )
            },
            label = { Text("Home") },
        )
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "camera",
            onClick = {
                if (permition.hasPermission) {
                    navController.navigate("camera")
                } else {
                    updatePermition()
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera",
                )
            },
            label = { Text("Image Search") },
        )
    }
}

@Composable
fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Column {
            Text(text = "Smart Dictionary\n", fontSize = 24.sp)
            Text("Welcome Name")
        }
        // TODO: add user name, retrieving from SQLite db
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
            text = NavRoute.Search,
            onItemClick = {
                navController.navigate(NavRoute.Search)
                close()
            },
        )
        DrawerMenuItem(
            iconDrawableId = Icons.Default.Camera,
            text = NavRoute.Camera,
            onItemClick = {
                navController.navigate(NavRoute.Camera)
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
