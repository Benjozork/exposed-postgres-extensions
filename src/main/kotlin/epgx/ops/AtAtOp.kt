package epgx.ops

import org.jetbrains.exposed.sql.ComparisonOp
import org.jetbrains.exposed.sql.Expression

/**
 * Represents an SQL operator that checks if [vector] matches [query].
 */
class AtAtOp(
        vector: Expression<*>,
        query: Expression<*>
) : ComparisonOp(vector, query, "@@")
