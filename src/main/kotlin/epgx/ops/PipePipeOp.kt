package epgx.ops

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryBuilder

/**
 * Represents an SQL operator that concatenates values in an immutable way, for use in generated columns or indices
 */
class PipePipeOp (
    private vararg val exprs: Expression<*>
) : Op<String>() {

    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append('(')
        for (expr in exprs.dropLast(1)) {
            queryBuilder.append(expr)
            queryBuilder.append(" || ")
        }
        queryBuilder.append(exprs.last())
        queryBuilder.append(')')
    }

}
