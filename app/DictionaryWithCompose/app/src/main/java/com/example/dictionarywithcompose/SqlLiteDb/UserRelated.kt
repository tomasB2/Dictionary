package com.example.dictionarywithcompose.SqlLiteDb // ktlint-disable package-name

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.WordOnScreen
import com.example.dictionarywithcompose.SqlLiteDb.dataTypes.UserLogin

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "my_database"
        private const val LIST_TABLE_NAME = "my_list_table"
        private const val COLUMN_LIST_ID = "list_id"
        private const val COLUMN_LIST_STRING = "list_word"
        private const val COLUMN_LIST_MEANING = "list_meaning"
        private const val COLUMN_LIST_EXAMPLE = "list_example"
        private const val LOGIN_TABLE_NAME = "my_login_table"
        private const val COLUMN_IS_LOGGED_IN = "is_logged_in"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PROFILE_IMAGE = "profile_image"
        private const val COLUMN_LAST_IMAGE_RESULT = "img_text"
        private const val LAST_IMAGE_TABLE_NAME = "last_image_table"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $LIST_TABLE_NAME ($COLUMN_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_LIST_STRING TEXT, $COLUMN_LIST_MEANING TEXT, $COLUMN_LIST_EXAMPLE TEXT)")
        db?.execSQL("CREATE TABLE $LOGIN_TABLE_NAME ($COLUMN_IS_LOGGED_IN INTEGER, $COLUMN_USERNAME TEXT, $COLUMN_PROFILE_IMAGE TEXT)")
        db?.execSQL("CREATE TABLE $LAST_IMAGE_TABLE_NAME ($COLUMN_LAST_IMAGE_RESULT TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //
    }
    fun insertLastImageResult(string: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LAST_IMAGE_RESULT, string)
        }
        db.insert(LAST_IMAGE_TABLE_NAME, null, values)
        db.close()
    }
    fun getLastImageResult(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $LAST_IMAGE_TABLE_NAME", null)
        var wordToReturn = ""
        cursor.use {
            if (it.moveToLast()) {
                val index = it.getColumnIndex(COLUMN_LAST_IMAGE_RESULT)
                if (index >= 0 && index <= it.columnCount) {
                    wordToReturn = it.getString(index)
                }
            }
        }
        db.close()
        return wordToReturn
    }
    fun insertString(string: WordOnScreen) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LIST_STRING, string.word)
            put(COLUMN_LIST_MEANING, string.meaning)
            put(COLUMN_LIST_EXAMPLE, string.example)
        }
        db.insert(LIST_TABLE_NAME, null, values)
        db.close()
    }
    fun getLastString(): WordOnScreen {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $LIST_TABLE_NAME", null)
        var wordToReturn = WordOnScreen()
        cursor.use {
            if (it.moveToLast()) {
                val index = it.getColumnIndex(COLUMN_LIST_STRING)
                val indexMeaning = it.getColumnIndex(COLUMN_LIST_MEANING)
                val indexExample = it.getColumnIndex(COLUMN_LIST_EXAMPLE)
                if (index >= 0 && index <= it.columnCount &&
                    indexMeaning >= 0 && indexMeaning <= it.columnCount &&
                    indexExample >= 0 && indexExample <= it.columnCount
                ) {
                    wordToReturn = WordOnScreen(it.getString(index), it.getString(indexMeaning), it.getString(indexExample))
                }
            }
        }
        db.close()
        return wordToReturn
    }
    fun getAllStrings(): List<WordOnScreen> {
        val list = mutableListOf<WordOnScreen>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $LIST_TABLE_NAME", null)
        cursor.use {
            while (it.moveToNext()) {
                val index = it.getColumnIndex(COLUMN_LIST_STRING)
                val indexMeaning = it.getColumnIndex(COLUMN_LIST_MEANING)
                val indexExample = it.getColumnIndex(COLUMN_LIST_EXAMPLE)
                if (index >= 0 && index <= it.columnCount &&
                    indexMeaning >= 0 && indexMeaning <= it.columnCount &&
                    indexExample >= 0 && indexExample <= it.columnCount
                ) {
                    val name = it.getString(index)
                    val meaning = it.getString(indexMeaning)
                    val example = it.getString(indexExample)
                    list.add(WordOnScreen(name, meaning, example))
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
