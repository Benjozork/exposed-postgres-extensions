package epgx.test.ops

import epgx.models.PgTable
import epgx.test.models.DatabaseConnectedTest

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PipePipeOpTest : DatabaseConnectedTest(TestTable) {

    object TestTable : PgTable("ha") {
        val first = text ("first_name")
        val middle = text ("middle_name")
        val last = text ("last_name")
        val title = text ("title")
    }

    @Test fun `should do || concat on two values properly`() {
        transaction {
            TestTable.insert {
                it[first] = "Mary"
                it[last] = "Jane"
                it[middle] = "Doe"
                it[title] = "M$."
            }
        }

        val expr = with(TestTable) { first + " " + last }

        val result = transaction {
            TestTable.slice(expr).selectAll().first()[expr]
        }

        assertEquals("Mary Jane", result)
    }

    @Test fun `should do || concat on four values properly`() {
        transaction {
            TestTable.insert {
                it[first] = "Mary"
                it[last] = "Jane"
                it[middle] = "Doe"
                it[title] = "M$."
            }
        }

        val expr = with(TestTable) { title + " " + first + " " + middle + " " + last }

        val result = transaction {
            TestTable.slice(expr).selectAll().first()[expr]
        }

        assertEquals("M$. Mary Doe Jane", result)
    }

}
