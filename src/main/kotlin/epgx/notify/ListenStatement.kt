package epgx.notify

import org.jetbrains.exposed.sql.statements.Statement
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi

import com.impossibl.postgres.api.jdbc.PGConnection
import com.impossibl.postgres.api.jdbc.PGNotificationListener

import org.jetbrains.exposed.sql.*

class ListenStatement(private val channelName: String, private val onNotification: (Int, String, String) -> Unit) : Statement<Unit>(StatementType.OTHER, emptyList()) {

    override fun arguments(): Iterable<Iterable<Pair<IColumnType, Any?>>> = emptyList()

    override fun prepareSQL(transaction: Transaction): String = QueryBuilder(true).apply {
        append("LISTEN \"$channelName\"")
    }.toString()

    override fun PreparedStatementApi.executeInternal(transaction: Transaction): Unit {
        executeUpdate()

        val ngConnection = (transaction.connection.connection as PGConnection)

        val listener = object : PGNotificationListener {
            override fun notification(processId: Int, channelName: String, payload: String) {
                onNotification(processId, channelName, payload)
            }
        }

        ngConnection.addNotificationListener(listener)
    }

}
