package epgx.functions

import org.jetbrains.exposed.sql.*

private fun castLanguage(language: String?) =
    Cast<String>(QueryParameter(language, TextColumnType()),
        object : ColumnType() {
            override fun sqlType() = "REGCONFIG"
        }
    )

/**
 * Creates a `tsquery` string from a column, using the `to_tsquery` function.
 *
 * @param language the `regconfig` to use
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch-controls.html#TEXTSEARCH-PARSING-DOCUMENTS)
 */
fun <T : String?> Expression<T>.toTsQuery(language: String? = null): CustomFunction<T> =
    if (language == null)
        CustomFunction("to_tsquery", TextColumnType(), this)
    else
        CustomFunction("to_tsquery", TextColumnType(), castLanguage(language), this)


/**
 * Creates a `tsvector` expression from a column, using the `plainto_tsquery` function.
 *
 * @param language the `regconfig` to use
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch-controls.html#TEXTSEARCH-PARSING-DOCUMENTS)
 */
fun <T : String?> Expression<T>.plainToTsQuery(language: String? = null): CustomFunction<T> =
    if (language == null)
        CustomFunction("plainto_tsquery", TextColumnType(), this)
    else
        CustomFunction("plainto_tsquery", TextColumnType(), castLanguage(language), this)

/**
 * Creates a `tsvector` expression from a column, using the `phraseto_tsquery` function.
 *
 * @param language the `regconfig` to use
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch-controls.html#TEXTSEARCH-PARSING-DOCUMENTS)
 */
fun <T : String?> Expression<T>.phraseToTsQuery(language: String? = null): CustomFunction<T> =
    if (language == null)
        CustomFunction("phraseto_tsquery", TextColumnType(), this)
    else
        CustomFunction("phraseto_tsquery", TextColumnType(), castLanguage(language), this)

/**
 * Creates a `tsvector` expression from a column, using the `websearch_to_tsquery` function.
 *
 * @param language the `regconfig` to use
 *
 * @author Benjozork
 *
 * [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch-controls.html#TEXTSEARCH-PARSING-DOCUMENTS)
 */
fun <T : String?> Expression<T>.webSearchToTsQuery(language: String? = null): CustomFunction<T> =
    if (language == null)
        CustomFunction("websearch_to_tsquery", TextColumnType(), this)
    else
        CustomFunction("websearch_to_tsquery", TextColumnType(), castLanguage(language), this)
