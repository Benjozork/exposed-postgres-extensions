package epgx.notify

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.Statement
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi

class NotifyStatement (
    private val channel: String,
    private val payload: Expression<*>
) : Statement<Unit>(StatementType.OTHER, emptyList()) {

    override fun arguments(): Iterable<Iterable<Pair<IColumnType, Any?>>> = QueryBuilder(true).run {
        payload.toQueryBuilder(this)
        listOf(args)
    }

    override fun prepareSQL(transaction: Transaction): String = QueryBuilder(true).apply {
        append("NOTIFY ")
        append("\"$channel\"")
        append(", ")
        payload.toQueryBuilder(this)
    }.toString()

    override fun PreparedStatementApi.executeInternal(transaction: Transaction): Unit? = executeUpdate().let { Unit }

}
