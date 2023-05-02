package com.example.demo.common.repository.implementations

import com.example.demo.common.repository.Transaction
import com.example.demo.friends.repo.FriendsMapper
import com.example.demo.friends.repo.FriendsRepoInterface
import com.example.demo.friends.repo.FriendsRepoSql
import com.example.demo.previousSearches.repo.PreviousSearchesImp
import com.example.demo.previousSearches.repo.PreviousSearchesInterface
import com.example.demo.previousSearches.repo.SearchMapper
import com.example.demo.user.repo.UserMapper
import com.example.demo.user.repo.UserRepositoryImp
import com.example.demo.user.repo.UserRepositoryInterface
import java.sql.Connection

class TransactionImp(
    private val connection: Connection,
) : Transaction {

    private val userMapper = UserMapper()
    override val usersRepository: UserRepositoryInterface by lazy { UserRepositoryImp(connection, userMapper) }

    private val previousSearchesMapper = SearchMapper()
    override val previousSearchesRepository: PreviousSearchesInterface by lazy { PreviousSearchesImp(connection, previousSearchesMapper) }

    private val friendsMapper = FriendsMapper()
    override val friendsRepository: FriendsRepoInterface by lazy { FriendsRepoSql(connection, friendsMapper) }
    override fun rollback() {
        connection.rollback()
        throw com.example.demo.common.domain.errors.ServerError(message = "Transaction roll back", cause = null)
    }
}
