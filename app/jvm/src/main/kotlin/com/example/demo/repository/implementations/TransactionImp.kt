package com.example.demo.repository.implementations

import com.example.demo.domain.errors.ServerError
import com.example.demo.repository.Transaction
import com.example.demo.repository.implementations.mappers.SearchMapper
import repository.PreviousSearchesInterface
import repository.UserRepositoryInterface
import repository.implementations.PreviousSearchesImp
import repository.implementations.UserRepositoryImp
import repository.implementations.mappers.UserMapper
import java.sql.Connection

class TransactionImp(
    private val connection: Connection,
) : Transaction {

    private val userMapper = UserMapper()
    override val usersRepository: UserRepositoryInterface by lazy { UserRepositoryImp(connection, userMapper) }

    private val previousSearchesMapper = SearchMapper()
    override val previousSearchesRepository: PreviousSearchesInterface by lazy { PreviousSearchesImp(connection, previousSearchesMapper) }

    override fun rollback() {
        connection.rollback()
        throw ServerError(message = "Transaction roll back", cause = null)
    }
}
