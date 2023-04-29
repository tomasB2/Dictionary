package com.example.demo.friends.service

import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.user.domain.User
import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("FriendsService")
class FriendsService : FriendsServiceInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun addFriend(userName: String, friendName: String): Response<Boolean> {
        logger.info("addFriend for: {} and {}", userName, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(message = "User to add not found", cause = ErrorCause.USER_NOT_FOUND))
                val friends = transaction.friendsRepository.getFriends(userName).res
                friends.addFriend(userName)
                transaction.friendsRepository.updateList(userName, friends)
            }
        } catch (e: Exception) {
            logger.error("addFriend for: {} and {}, e: {}", userName, friendName, e.message)
            throw e
        }
    }

    override fun removeFriend(userName: String, friendName: String): Response<Boolean> {
        logger.info("removeFriend for: {} and {}", userName, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(message = "User to remove not found", cause = ErrorCause.USER_NOT_FOUND))
                val friends = transaction.friendsRepository.getFriends(userName).res
                friends.removeFriend(userName)
                transaction.friendsRepository.updateList(userName, friends)
            }
        } catch (e: Exception) {
            logger.error("removeFriend for: {} and {}, e: {}", userName, friendName, e.message)
            throw e
        }
    }

    override fun getFriends(userName: String): Response<List<User>?> {
        logger.info("getFriends for: {}", userName)
        return try {
            TransactionManagerImp.run { transaction ->
                val list = transaction.friendsRepository.getFriends(userName)
                val userList = list.res.list.map {
                    val user = transaction.usersRepository.getUserByName(it).res
                        ?: return@run Response(res = null, e = ErrorResponse(message = "User not found", cause = ErrorCause.USER_NOT_FOUND))
                    user
                }
                Response(res = userList, e = null)
            }
        } catch (e: Exception) {
            logger.error("getFriends for: {}, e: {}", userName, e.message)
            throw e
        }
    }
}
