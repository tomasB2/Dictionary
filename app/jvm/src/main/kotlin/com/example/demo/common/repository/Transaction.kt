package com.example.demo.common.repository

import com.example.demo.friends.repo.FriendsRepoInterface
import com.example.demo.previousSearches.repo.PreviousSearchesInterface
import com.example.demo.user.repo.UserRepositoryInterface

interface Transaction {

    val usersRepository: UserRepositoryInterface
    val previousSearchesRepository: PreviousSearchesInterface
    val friendsRepository: FriendsRepoInterface

    fun rollback()
}
