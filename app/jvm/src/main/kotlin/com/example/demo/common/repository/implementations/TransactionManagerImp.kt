package com.example.demo.common.repository.implementations

import com.example.demo.common.repository.Transaction
import com.example.demo.common.repository.TransactionManager
import com.example.demo.common.repository.utils.getNewConnection
import org.springframework.stereotype.Component
import java.sql.Connection

@Component
object TransactionManagerImp : TransactionManager {
    override fun <R> run(block: (Transaction) -> R): R {
        val conn = getNewConnection()
        try {
            if (conn == null) {
                throw Exception("Cant connect with data base")
            }
            conn.autoCommit = false
            conn.transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ
            val transaction = TransactionImp(conn)
            val result = block(transaction)
            conn.commit()
            conn.close()
            return result
        } catch (e: Exception) {
            conn?.rollback()
            conn?.close()
            throw e
        }
    }
}
