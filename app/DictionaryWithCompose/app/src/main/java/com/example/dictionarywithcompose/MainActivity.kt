package com.example.dictionarywithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.dictionarywithcompose.Activities.SelectionMenu.MainNavigatorScreen
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * private fun checkIfDarkMode(context: Context): Boolean {
         val sharedPref = context.getSharedPreferences("isDarkMode", Context.MODE_PRIVATE)
         val isDarkMode = sharedPref?.getBoolean("isDark", false)
         return isDarkMode == null || isDarkMode == false
         }
         private fun changeColorMode(activity: Context, value: Boolean) {
         val sharedPref = activity.getSharedPreferences("isDarkMode", Context.MODE_PRIVATE)
         sharedPref.edit()?.putBoolean("isDark", value)?.apply()
         }

         */
        setContent {
            ThemeConstructor {
                // A surface container using the 'background' color from the theme

                // AuthComposable()
                MainNavigatorScreen(LocalContext.current)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}
