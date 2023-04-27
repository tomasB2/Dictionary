package repository.implementations.mappers

import com.example.demo.domain.UserImg
import domain.User
import org.slf4j.LoggerFactory
import java.sql.ResultSet

private val logger = LoggerFactory.getLogger(UserMapper::class.java)

/**
 * Class for mapping user data from database to User object
 */
class UserMapper {
    fun map(rs: ResultSet): User {
        logger.info("It got out")
        return User(
            name = rs.getString("name"),
            password_verification = rs.getString("password_verification"),
            email = rs.getString("email"),
        )
    }

    fun mapImg(rs: ResultSet): UserImg {
        return UserImg(
            name = rs.getString("name"),
            data = rs.getString("data"),
        )
    }
}
