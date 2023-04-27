package com.example.dictionarywithcompose.SqlLiteDb // ktlint-disable package-name

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dictionarywithcompose.SqlLiteDb.dataTypes.UserLogin

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "my_database"
        private const val LIST_TABLE_NAME = "my_list_table"
        private const val COLUMN_LIST_ID = "list_id"
        private const val COLUMN_LIST_STRING = "list_string"
        private const val LOGIN_TABLE_NAME = "my_login_table"
        private const val COLUMN_IS_LOGGED_IN = "is_logged_in"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PROFILE_IMAGE = "profile_image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $LIST_TABLE_NAME ($COLUMN_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_LIST_STRING TEXT)")
        db?.execSQL("CREATE TABLE $LOGIN_TABLE_NAME ($COLUMN_IS_LOGGED_IN INTEGER, $COLUMN_USERNAME TEXT, $COLUMN_PROFILE_IMAGE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
    }

    fun insertString(string: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LIST_STRING, string)
        }
        db.insert(LIST_TABLE_NAME, null, values)
        db.close()
    }

    fun getAllStrings(): List<String> {
        val list = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $LIST_TABLE_NAME", null)
        cursor.use {
            while (it.moveToNext()) {
                val index = it.getColumnIndex(COLUMN_LIST_STRING)
                if (index >= 0 && index <= it.columnCount) {
                    val string = it.getString(index)
                    list.add(string)
                }
            }
        }
        db.close()
        return list
    }

    fun insertUserLogin(userLogin: UserLogin) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_LOGGED_IN, userLogin.isLoggedIn)
            put(COLUMN_USERNAME, userLogin.username)
            put(COLUMN_PROFILE_IMAGE, userLogin.profileImage)
        }
        db.insert(LOGIN_TABLE_NAME, null, values)
        db.close()
    }

    fun getUserLogin(): UserLogin {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $LOGIN_TABLE_NAME", null)
        var userLogin = UserLogin()
        cursor.use {
            if (it.moveToFirst()) {
                val index1 = it.getColumnIndex(COLUMN_IS_LOGGED_IN)
                val index2 = it.getColumnIndex(COLUMN_USERNAME)
                val index3 = it.getColumnIndex(COLUMN_PROFILE_IMAGE)
                if (index1 >= 0 && index1 <= it.columnCount && index2 >= 0 && index2 <= it.columnCount && index3 >= 0 && index3 <= it.columnCount) {
                    val isLoggedIn = it.getInt(index1) == 1
                    val username = it.getString(index2)
                    val profileImage = it.getString(index3)
                    userLogin = UserLogin(isLoggedIn, username, profileImage)
                }
            }
        }
        db.close()
        return userLogin
    }
}
