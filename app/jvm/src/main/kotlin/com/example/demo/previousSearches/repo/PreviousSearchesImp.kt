package com.example.demo.previousSearches.repo

import com.example.demo.common.domain.Response
import com.example.demo.common.repository.utils.PreviousSearchesQueris
import com.example.demo.common.repository.utils.createInDataBase
import com.example.demo.common.repository.utils.deleteInDataBase
import com.example.demo.common.repository.utils.getInDataBase
import com.example.demo.meanings.domain.model.WordInfo
import org.postgresql.util.PGobject
import java.sql.Connection

class PreviousSearchesImp(
    private val connection: Connection,
    private val searchMapper: SearchMapper,
) : PreviousSearchesInterface {
    override fun wordExistsInUser(userId: Int, searchKey: String): Response<Boolean> {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.WORD_EXIST_IN_USER)
        stm.setInt(1, userId)
        stm.setString(2, searchKey)
        val res = getInDataBase(
            stm = stm,
        )
        return Response(res = res.next(), e = null)
    }

    override fun getPreviousSearch(searchKey: String): Response<WordInfo?> {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.GET_PREVIOUS_SEARCH)
        stm.setString(1, searchKey)
        val res = getInDataBase(
            stm = stm,
        )
        return Response(res = if (res.next()) searchMapper.map(res) else null, e = null)
    }

    override fun getPreviousSearches(userId: Int): Response<List<WordInfo?>?> {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.GET_PREVIOUS_SEARCHES)
        stm.setInt(1, userId)
        val res = getInDataBase(
            stm = stm,
        )
        return Response(res = searchMapper.mapMany(res), e = null)
    }

    override fun addPreviousSearch(userId: Int, searchKey: String, search: WordInfo) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.ADD_PREVIOUS_SEARCH)
        val json = PGobject()
        json.type = "json"
        json.value = search.toJson()
        stm.setInt(1, userId)
        stm.setString(2, searchKey)
        stm.setObject(3, json)
        createInDataBase(
            stm = stm,
        )
    }

    override fun deletePreviousSearch(userId: Int, searchKey: String) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.DELETE_PREVIOUS_SEARCH)
        stm.setInt(1, userId)
        stm.setString(2, searchKey)
        deleteInDataBase(
            stm = stm,
        )
    }

    override fun deleteAllPreviousSearches(userId: Int) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.DELETE_PREVIOUS_SEARCHES)
        stm.setInt(1, userId)
        deleteInDataBase(
            stm = stm,
        )
    }
}
