package com.example.demo.repository

import repository.PreviousSearchesInterface
import repository.UserRepositoryInterface

interface Transaction {

    val usersRepository: UserRepositoryInterface
    val previousSearchesRepository: PreviousSearchesInterface

    fun rollback()
}