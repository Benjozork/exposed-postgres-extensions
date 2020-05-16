package epgx.functions

import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.IntegerColumnType

// Some standard SQL functions, but not present in Exposed.

/**
 * Turns this string expression into its bit length.
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/functions-string.html)
 */
fun <T : String?> Expression<T>.bitLength() = CustomFunction<Int>("BIT_LENGTH", IntegerColumnType(), this)

/**
 * Turns this string expression into its character length.
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/functions-string.html)
 */
fun <T : String?> Expression<T>.charLength() = CustomFunction<Int>("CHAR_LENGTH", IntegerColumnType(), this)

/**
 * Turns this string expression into its octet length.
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/functions-string.html)
 */
fun <T : String?> Expression<T>.octetLength() = CustomFunction<Int>("OCTET_LENGTH", IntegerColumnType(), this)
