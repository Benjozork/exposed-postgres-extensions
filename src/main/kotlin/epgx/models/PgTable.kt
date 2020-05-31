package epgx.models

import epgx.models.values.TsVector
import epgx.ops.PipePipeOp
import epgx.types.Generated
import epgx.types.JsonbColumnType
import epgx.types.TsVectorColumnType
import org.jetbrains.exposed.sql.*

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
     * Creates a column storing instances of [T] using the [JsonbColumnType] type.
     *
     * @param serializer   see [JsonbColumnType]
     * @param deserializer see [JsonbColumnType]
     */
    fun <T : Any?> jsonb(name: String, serializer: (T) -> String, deserializer: (String) -> T): Column<T> {
        return registerColumn(name, JsonbColumnType(serializer, deserializer))
    }

    /**
     * Creates a column storing instances of [T] using the [JsonbColumnType] type.
     *
     * @param converter see [JsonbColumnType]
     */
    fun <T : Any?> jsonb(name: String, converter: JsonbColumnType.Converter<T>): Column<T> {
        return registerColumn(name, JsonbColumnType(converter::serializer, converter::deserializer))
    }

    /**
     * Creates a column storing plain JSON strings using the [JsonbColumnType] type.
     */
    fun jsonb(name: String): Column<String> = registerColumn(name, JsonbColumnType({ it }, { it }))

    /**
     * Creates a column using the [TsVectorColumnType] type.
     */
    fun tsvector(name: String): Column<TsVector> = registerColumn(name, TsVectorColumnType())

    /**
     * Concatenates [this] and [other] using [PipePipeOp]
     */
    operator fun Expression<out String?>.plus(other: Expression<out String?>): PipePipeOp =
            PipePipeOp(this, other)

    /**
     * Concatenates [this] and [other] using [PipePipeOp]
     */
    operator fun Expression<out String?>.plus(other: String): PipePipeOp =
            PipePipeOp(this, stringLiteral(other))

}
