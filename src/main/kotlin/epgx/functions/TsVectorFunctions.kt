package epgx.functions

import epgx.types.TsVectorColumnType

import org.jetbrains.exposed.sql.*

/**
 * Creates a `tsvector` expression from a [String] expression.
 *
 * @param language the `regconfig` to use
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch-controls.html#TEXTSEARCH-PARSING-DOCUMENTS)
 */
fun <T : String?> Expression<T>.toTsVector(language: String? = null): CustomFunction<TsVectorColumnType> =
    if (language == null)
        CustomFunction("to_tsvector", TsVectorColumnType(), this)
    else
        CustomFunction("to_tsvector", TsVectorColumnType(), QueryParameter(language, TextColumnType()), this)
