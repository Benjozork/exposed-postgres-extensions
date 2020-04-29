package epgx.models

import epgx.types.Generated
import epgx.types.Jsonb

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table

/**
 * Wrapper around [Exposed's `Table`][Table] to include postgres-specific extension functions on the column DSL
 *
 * @author Benjozork
 */
open class PgTable(name: String = "") : Table(name) {

    @Suppress("UNCHECKED_CAST")
    fun <T, C : Column<T>> C.generated(generator: SqlExpressionBuilder.() -> Expression<*>): C {
        val newColumn: C = Column<T>(table, name, Generated(this.columnType, generator)) as C

        newColumn.foreignKey = foreignKey

        return replaceColumn(this, newColumn)
    }

    /**
     * Creates a column storing instances of [T] using the [Jsonb] type.
     *
     * @param serializer   see [Jsonb]
     * @param deserializer see [Jsonb]
     */
    fun <T : Any?> jsonb(name: String, serializer: (T) -> String, deserializer: (String) -> T): Column<T> {
        return registerColumn(name, Jsonb(serializer, deserializer))
    }

    /**
     * Creates a column storing instances of [T] using the [Jsonb] type.
     *
     * @param converter see [Jsonb]
     */
    fun <T : Any?> jsonb(name: String, converter: Jsonb.Converter<T>): Column<T> {
        return registerColumn(name, Jsonb(converter::serializer, converter::deserializer))
    }

    /**
     * Creates a column storing plain JSON strings using the [Jsonb] type.
     */
    fun jsonb(name: String): Column<String> = registerColumn(name, Jsonb({ it }, { it }))

}
