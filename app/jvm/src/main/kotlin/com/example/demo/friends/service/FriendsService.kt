package com.example.demo.friends.service

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.utils.authenticate
import com.example.demo.user.domain.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("FriendsService")
class FriendsService : FriendsServiceInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun addFriend(token: String, friendName: String): Response<Boolean> {
        logger.info("addFriend for: {} and {}", token, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = authenticate(token, transaction)
                if (user.res == null) return@run Response(false, user.e)
                val username = user.res.name
                val friend = transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(error = "User to add not found", cause = ErrorCause.USER_NOT_FOUND))
                val friendId = friend.id
                transaction.friendsRepository.getFriends(friendId).res.let {
                    if (it != null) {
                        if (it.contains(username)) return@run Response(res = false, e = ErrorResponse(error = "User already added", cause = ErrorCause.USER_ALREADY_ADDED))
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't add user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
                transaction.friendsRepository.getFriendRequests(friendId).res.let {
                    if (it != null) {
                        if (it.contains(username)) return@run Response(res = false, e = ErrorResponse(error = "Can't send multiple requests", cause = ErrorCause.USER_ALREADY_REQUESTED))
                        it.addRequest(username)
                        transaction.friendsRepository.updateRequests(friendId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't add user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("addFriend for: {} and {}, e: {}", token, friendName, e.message)
            throw e
        }
    }

    override fun removeFriend(token: String, friendName: String): Response<Boolean> {
        logger.info("removeFriend for: {} and {}", token, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = authenticate(token, transaction)
                if (user.res == null) return@run Response(false, user.e)
                val userId = user.res.id
                val username = user.res.name
                val friend = transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(error = "User to remove not found", cause = ErrorCause.USER_NOT_FOUND))
                val friendId = friend.id
                transaction.friendsRepository.getFriends(userId).res.let {
                    println(it)
                    if (it != null) {
                        if (!it.contains(friendName)) return@run Response(res = false, e = ErrorResponse(error = "User not added", cause = ErrorCause.USER_NOT_ADDED))
                        it.removeFriend(friendName)
                        transaction.friendsRepository.updateFriendsList(userId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't remove user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
                transaction.friendsRepository.getFriends(friendId).res.let {
                    if (it != null) {
                        if (!it.contains(username)) return@run Response(res = false, e = ErrorResponse(error = "User not added", cause = ErrorCause.USER_NOT_ADDED))
                        it.removeFriend(username)
                        transaction.friendsRepository.updateFriendsList(friendId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't remove user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("removeFriend for: {} and {}, e: {}", token, friendName, e.message)
            throw e
        }
    }

    override fun getFriends(userName: String): Response<List<User>?> {
        logger.info("getFriends for: {}", userName)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = transaction.usersRepository.getUserByName(userName).res
                    ?: return@run Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
                val list = transaction.friendsRepository.getFriends(user.id)
                val userList = list.res?.list?.filter { it.isNotBlank() }?.map {
                    transaction.usersRepository.getUserByName(it).res
                        ?: return@run Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
                }
                Response(res = userList, e = null)
            }
        } catch (e: Exception) {
            logger.error("getFriends for: {}, e: {}", userName, e.message)
            throw e
        }
    }

    override fun getFriendRequests(token: String): Response<List<User>?> {
        logger.info("getFriendRequests for: {}", token)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = authenticate(token, transaction)
                if (user.res == null) return@run Response(null, user.e)
                val userId = user.res.id
                val list = transaction.friendsRepository.getFriendRequests(userId)
                val userList = list.res?.list?.map {
                    val curr = transaction.usersRepository.getUserByName(it).res
                        ?: return@run Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
                    curr
                }
                Response(res = userList, e = null)
            }
        } catch (e: Exception) {
            logger.error("getFriendRequests for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun acceptFriendRequest(token: String, friendName: String): Response<Boolean> {
        logger.info("acceptFriendRequest for: {} and {}", token, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = authenticate(token, transaction)
                if (user.res == null) return@run Response(false, user.e)
                val userId = user.res.id
                val userName = user.res.name
                val friend = transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(error = "User to accept not found", cause = ErrorCause.USER_NOT_FOUND))
                val friendId = friend.id
                transaction.friendsRepository.getFriendRequests(userId).res.let {
                    if (it != null) {
                        if (!it.contains(friendName)) return@run Response(res = false, e = ErrorResponse(error = "User not requested", cause = ErrorCause.NO_USER_REQUEST))
                        it.removeRequest(friendName)
                        transaction.friendsRepository.updateRequests(userId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't accept user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
                transaction.friendsRepository.getFriends(userId).res.let {
                    if (it != null) {
                        if (it.contains(friendName)) return@run Response(res = false, e = ErrorResponse(error = "User already added", cause = ErrorCause.USER_ALREADY_ADDED))
                        it.addFriend(friendName)
                        transaction.friendsRepository.updateFriendsList(userId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't accept user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
                transaction.friendsRepository.getFriends(friendId).res.let {
                    if (it != null) {
                        if (it.contains(userName)) return@run Response(res = false, e = ErrorResponse(error = "User already added", cause = ErrorCause.USER_ALREADY_ADDED))
                        it.addFriend(userName)
                        transaction.friendsRepository.updateFriendsList(friendId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't accept user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("acceptFriendRequest for: {} and {}, e: {}", token, friendName, e.message)
            throw e
        }
    }

    override fun declineFriendRequest(token: String, friendName: String): Response<Boolean> {
        logger.info("declineFriendRequest for: {} and {}", token, friendName)
        return try {
            TransactionManagerImp.run { transaction ->
                val user = authenticate(token, transaction)
                if (user.res == null) return@run Response(false, user.e)
                val userId = user.res.id
                transaction.usersRepository.getUserByName(friendName).res
                    ?: return@run Response(res = false, e = ErrorResponse(error = "User to decline not found", cause = ErrorCause.USER_NOT_FOUND))
                transaction.friendsRepository.getFriendRequests(userId).res.let {
                    if (it != null) {
                        if (!it.contains(friendName)) return@run Response(res = false, e = ErrorResponse(error = "User not added", cause = ErrorCause.NO_USER_REQUEST))
                        it.removeRequest(friendName)
                        transaction.friendsRepository.updateRequests(userId, it)
                    } else {
                        return@run Response(res = false, e = ErrorResponse(error = "Couldn't decline user", cause = ErrorCause.SERVER_ERROR))
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("declineFriendRequest for: {} and {}, e: {}", token, friendName, e.message)
            throw e
        }
    }
}
