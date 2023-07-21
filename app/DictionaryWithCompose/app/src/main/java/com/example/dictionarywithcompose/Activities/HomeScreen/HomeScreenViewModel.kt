package com.example.dictionarywithcompose.Activities.HomeScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.ClickEvent
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.DbState
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.HomeState
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow

class HomeScreenViewModel : ViewModel() {
    val state = MutableStateFlow(HomeState())
    val CurrentList = MutableStateFlow(DbState())

    fun getLastSearches(context: Context): List<MeaningContent> {
        val dbHelper = MyDatabaseHelper(context)
        return dbHelper.getRecentStrings()
    }
    fun updateState(context: Context) {
        val list = getLastSearches(context)
        Log.v("idk", "updateState: ${list.size}")
        if (list.isEmpty()) {
            state.value = state.value.copy(
                lastSearches = MeaningContent(),
                isfirst = true,
                islast = true,
            )
            return
        }
        if (list == CurrentList.value.lastSearches) {
            state.value = state.value.copy(
                lastSearches = list[CurrentList.value.currentIndex],
                isfirst = CurrentList.value.currentIndex == 0,
                islast = CurrentList.value.currentIndex == CurrentList.value.lastSearches.size - 1,
            )
            return
        }
        CurrentList.value = CurrentList.value.copy(
            lastSearches = list,
            currentIndex = 0,
        )
        state.value = state.value.copy(
            lastSearches = list[0],
            isfirst = CurrentList.value.currentIndex == 0,
            islast = CurrentList.value.currentIndex == CurrentList.value.lastSearches.size - 1,
        )
        Log.v("HomeScreenViewModel", "updateState: ${state.value.islast}")
        Log.v("HomeScreenViewModel", "updateState: ${state.value.isfirst}")
    }
    fun updateCurrentIndex(index: Int) {
        CurrentList.value = CurrentList.value.copy(
            currentIndex = index,
        )
        state.value = state.value.copy(
            lastSearches = CurrentList.value.lastSearches[index],
            isfirst = index == 0,
            islast = index == 10,
        )
        Log.v("HomeScreenViewModel", "updateCurrentIndex: ${state.value.isfirst}")
    }
    fun changeOnClick(click: ClickEvent) {
        when (click) {
            is ClickEvent.Next -> {
                if (CurrentList.value.currentIndex < CurrentList.value.lastSearches.size - 1) {
                    updateCurrentIndex(CurrentList.value.currentIndex + 1)
                }
            }
            is ClickEvent.Previous -> {
                if (CurrentList.value.currentIndex > 0) {
                    updateCurrentIndex(CurrentList.value.currentIndex - 1)
                }
            }
        }
    }

    fun isEmpty(context: Context): Boolean {
        updateState(context)
        return CurrentList.value.lastSearches.isEmpty()
    }
}
private fun List<MeaningContent>.searchForItem(item: MeaningContent): Int {
    for (i in this.indices) {
        if (this[i] == item) {
            return i
        }
    }
    return -1
}
