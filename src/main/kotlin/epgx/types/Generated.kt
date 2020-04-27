package epgx.types

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect

/**
 * Allows to create a PostgreSQL `generated` column.
 *
 * @author Benjozork
 *
 * @property baseType  the base [type][ColumnType] of the generated column, aka. its return type
 * @property generator a function to provide the expression to use in `generated always as ( <expr> )`
 */
open class Generated (
    private val baseType: IColumnType,
    private val generator: SqlExpressionBuilder.() -> Expression<*>
) : IColumnType by baseType {

    init {
        assert(TransactionManager.current().db.dialect is PostgreSQLDialect) { "Generated columns are only available in the PostgreSQL dialect" }
        assert(!baseType.nullable) { "Nullable generated columns are currently not supported" }
    }

    override fun sqlType(): String =
        "${baseType.sqlType()} GENERATED ALWAYS AS " +
        "( " +
        SqlExpressionBuilder.generator() +
        " ) STORED"

    override fun valueToDB(value: Any?) =
        if (value != null) error("Generated columns cannot be written to")
        else baseType.valueToDB(value)

    override fun notNullValueToDB(value: Any) = error("Generated columns cannot be written to")

}
