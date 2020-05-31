package epgx.functions

import epgx.ops.AtAtOp

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CustomFunction

/**
 * Checks if [this] tsvector column matches [tsQuery] query.
 */
@Suppress("FunctionName")
infix fun <T> Column<T>.`@@`(tsQuery: CustomFunction<*>): AtAtOp =
        AtAtOp(this, tsQuery)
