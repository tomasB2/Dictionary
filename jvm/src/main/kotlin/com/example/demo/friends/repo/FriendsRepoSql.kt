package com.example.demo.friends.repo

import com.example.demo.friends.Friends
import com.example.demo.common.domain.Response
import org.postgresql.util.PGobject
import org.slf4j.LoggerFactory
import com.example.demo.common.repository.utils.createInDataBase
import com.example.demo.common.repository.utils.updateInDataBase
import java.sql.Connection

class FriendsRepoSql(
    private val connection: Connection,
    private val friendsMapper: FriendsMapper,
) : FriendsRepoInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createFriendsList(userName: String, friendList: Friends): Response<Boolean> {
        logger.info("createFriendsList for: {}", userName)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.CREATE_FRIENDS_LIST)
            val json = PGobject()
            json.type = "json"
            json.value = friendList.toJson()
            stm.setString(1, userName)
            stm.setObject(2, json)
            createInDataBase(
                stm = stm,
            )
            Response(res = true, e = null)
        } catch (e: Exception) {
            logger.error("Error creating friends list for user: {}, e: {}", userName, e.message)
            throw e
        }
    }

    override fun updateList(userName: String, friendList: Friends): Response<Boolean> {
        logger.info("updateList for: {}", userName)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.UPDATE_FRIENDS)
            val json = PGobject()
            json.type = "json"
            json.value = friendList.toJson()
            stm.setString(1, userName)
            stm.setObject(2, json)
            updateInDataBase(
                stm = stm,
            )
            Response(res = true, e = null)
        } catch (e: Exception) {
            logger.error("Error updating friends list for user: {}, e: {}", userName, e.message)
            throw e
        }
    }

    override fun getFriends(userName: String): Response<Friends> {
        logger.info("getFriends for: {}", userName)
        return try {
            val stm = connection
                .prepareStatement(FriendsQueries.GET_FRIENDS)
            stm.setString(1, userName)
            val res = stm.executeQuery()
            Response(res = friendsMapper.map(res), e = null)
        } catch (e: Exception) {
            logger.error("Error getting friends list for user: {}, e: {}", userName, e.message)
            throw e
        }
    }
}
