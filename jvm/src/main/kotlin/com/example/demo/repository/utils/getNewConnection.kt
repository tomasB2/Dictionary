package repository.utils // ktlint-disable filename

import org.postgresql.ds.PGSimpleDataSource
import java.sql.Connection

/**
 * Function that retrieves a connections to the database
 * @return the connection
 */
fun getNewConnection(): Connection? {
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
    dataSource.setURL(jdbcDatabaseURL)

    return dataSource.connection
}
