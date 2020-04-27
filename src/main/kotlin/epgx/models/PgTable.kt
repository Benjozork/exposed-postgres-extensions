package epgx.models

import epgx.types.Generated

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table

/**
 * Wrapper around [Exposed's `Table`][Table] to include postgres-specific extension functions on the column DSL
 *
 * @author Benjozork
 */
open class PgTable : Table() {

    @Suppress("UNCHECKED_CAST")
    fun <T, C : Column<T>> C.generated(generator: SqlExpressionBuilder.() -> Expression<*>): C {
        val newColumn: C = Column<T>(table, name, Generated(this.columnType, generator)) as C

        newColumn.foreignKey = foreignKey

        return replaceColumn(this, newColumn)
    }

}
