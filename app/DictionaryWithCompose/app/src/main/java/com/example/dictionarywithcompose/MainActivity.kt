package com.example.dictionarywithcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.dictionarywithcompose.Activities.Auth.AuthActivity
import com.example.dictionarywithcompose.Activities.SelectionMenu.NavigationDrawer
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val dbHelper = MyDatabaseHelper(this)
        val splashScreen = installSplashScreen()
        val loggedIn = dbHelper.getUserLogin()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        if (!loggedIn.isLoggedIn) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        } else {
            val intent = Intent(this, NavigationDrawer::class.java)
            intent.putExtra("username", loggedIn.username)
            startActivity(intent)
            finish()
        }
    }
}
