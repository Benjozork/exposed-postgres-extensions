package epgx.functions

import epgx.ops.PipePipeOp

import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.stringLiteral

// Some standard SQL functions, but not present in Exposed.

/**
 * Concatenates [this] and [other] using [PipePipeOp]
 */
@Suppress("FunctionName")
infix fun Expression<String>.`||`(other: Expression<String>): PipePipeOp =
        PipePipeOp(this, other)

/**
 * Concatenates [this] and [other] using [PipePipeOp]
 */
@Suppress("FunctionName")
infix fun Expression<String>.`||`(other: String): PipePipeOp =
        PipePipeOp(this, stringLiteral(other))

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
