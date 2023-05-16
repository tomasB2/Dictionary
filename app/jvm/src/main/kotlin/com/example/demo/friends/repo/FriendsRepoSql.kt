package com.example.demo.friends.repo

import com.example.demo.common.domain.Response
import com.example.demo.common.repository.utils.createInDataBase
import com.example.demo.common.repository.utils.updateInDataBase
import com.example.demo.friends.FriendRequests
import com.example.demo.friends.Friends
import org.postgresql.util.PGobject
import org.slf4j.LoggerFactory
import java.sql.Connection

class FriendsRepoSql(
    private val connection: Connection,
    private val friendsMapper: FriendsMapper,
) : FriendsRepoInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createFriendsList(userId: Int, friendList: Friends): Response<Boolean> {
        logger.info("createFriendsList for: {}", userId)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.CREATE_FRIENDS_LIST)
            val json = PGobject()
            json.type = "json"
            json.value = friendList.toJson()
            stm.setInt(1, userId)
            stm.setObject(2, json)
            stm.setObject(3, json)
            createInDataBase(
                stm = stm,
            )
            Response(res = true, e = null)
        } catch (e: Exception) {
            logger.error("Error creating friends list for user: {}, e: {}", userId, e.message)
            throw e
        }
    }

    override fun updateFriendsList(userId: Int, friendList: Friends): Response<Boolean> {
        logger.info("updateList for: {}", userId)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.UPDATE_FRIENDS)
            val json = PGobject()
            json.type = "json"
            json.value = friendList.toJson()
            stm.setObject(1, json)
            stm.setInt(2, userId)
            updateInDataBase(
                stm = stm,
            )
            Response(res = true, e = null)
        } catch (e: Exception) {
            logger.error("Error updating friends list for user: {}, e: {}", userId, e.message)
            throw e
        }
    }

    override fun getFriends(userId: Int): Response<Friends?> {
        logger.info("getFriends for: {}", userId)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.GET_FRIENDS)
            stm.setInt(1, userId)
            val res = stm.executeQuery()
            res.next()
            Response(res = friendsMapper.mapFriends(res), e = null)
        } catch (e: Exception) {
            logger.error("Error getting friends list for user: {}, e: {}", userId, e.message)
            throw e
        }
    }

    override fun getFriendRequests(userId: Int): Response<FriendRequests?> {
        logger.info("getFriendRequests for: {}", userId)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.GET_FRIEND_REQUESTS)
            stm.setInt(1, userId)
            val res = stm.executeQuery()
            res.next()
            Response(res = friendsMapper.mapRequests(res), e = null)
        } catch (e: Exception) {
            logger.error("Error getting friend requests for user: {}, e: {}", userId, e.message)
            throw e
        }
    }

    override fun updateRequests(userId: Int, requests: FriendRequests): Response<Boolean> {
        logger.info("removeFromRequests for: {}", userId)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.UPDATE_FROM_REQUESTS)
            val json = PGobject()
            json.type = "json"
            json.value = requests.toJson()
            stm.setObject(1, json)
            stm.setInt(2, userId)
            updateInDataBase(
                stm = stm,
            )
            Response(res = true, e = null)
        } catch (e: Exception) {
            logger.error("Error removing friend requests for user: {}, e: {}", userId, e.message)
            throw e
        }
    }
}
