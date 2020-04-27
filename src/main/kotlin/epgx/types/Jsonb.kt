package epgx.types

import org.jetbrains.exposed.sql.ColumnType

import org.postgresql.util.PGobject

/**
 * Allows to create a PostgreSQL `jsonb` column.
 *
 * In order to be library-agnostic, this requires both a [serializer] and a [deserializer] to be provided.
 *
 * @author Benjozork
 *
 * @property serializer   a function that converts instances of [T] to a JSON string
 * @property deserializer a function that converts a JSON string to an instance of [T]
 */
class Jsonb<T : Any?> (
    private val serializer: (T) -> String,
    private val deserializer: (String) -> T
) : ColumnType() {

    override fun sqlType() = "JSONB"

    @Suppress("UNCHECKED_CAST")
    override fun notNullValueToDB(value: Any): PGobject =
            PGobject().apply { type = "jsonb"; this.value = serializer(value as T) }

    @Suppress("UNCHECKED_CAST")
    override fun nonNullValueToString(value: Any): String =
            (value as T).let(serializer).let { "\'$it\'" }

    override fun valueFromDB(value: Any): Any =
            value.toString().let(deserializer) as Any

}
