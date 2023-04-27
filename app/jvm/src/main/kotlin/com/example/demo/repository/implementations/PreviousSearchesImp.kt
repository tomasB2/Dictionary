package repository.implementations

import com.example.demo.domain.meanings.model.WordInfo
import com.example.demo.repository.implementations.mappers.SearchMapper
import com.example.demo.repository.utils.PreviousSearchesQueris
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import org.postgresql.util.PGobject
import repository.PreviousSearchesInterface
import repository.implementations.mappers.UserMapper
import repository.utils.createInDataBase
import repository.utils.getInDataBase
import java.sql.Connection

class PreviousSearchesImp(
    private val connection: Connection,
    private val searchMapper: SearchMapper,
) : PreviousSearchesInterface {
    override fun getPreviousSearches(userName: String): Response<List<WordInfo>> {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.GET_PREVIOUS_SEARCHES)
        stm.setString(1, userName)
        val res = getInDataBase(
            stm = stm,
        )
        return Response(res = searchMapper.mapMany(res), e = null)
    }

    override fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.ADD_PREVIOUS_SEARCH)
        val json = PGobject()
        json.type = "json"
        json.value = search.toJson()
        stm.setString(1, userName)
        stm.setString(2, searchKey)
        stm.setObject(3, json)
        createInDataBase(
            stm = stm,
        )
    }

    override fun deletePreviousSearch(userName: String, searchKey: String) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.DELETE_PREVIOUS_SEARCH)
        stm.setString(1, userName)
        stm.setString(2, searchKey)
        createInDataBase(
            stm = stm,
        )
    }

    override fun deleteAllPreviousSearches(userName: String) {
        val stm = connection
            .prepareStatement(PreviousSearchesQueris.DELETE_PREVIOUS_SEARCHES)
        stm.setString(1, userName)
        createInDataBase(
            stm = stm,
        )
    }
}
