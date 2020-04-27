package epgx.test.types

import epgx.models.PgTable
import epgx.functions.charLength
import epgx.test.models.DatabaseConnectedTest

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GeneratedColumnTest : DatabaseConnectedTest() {

    @Test fun `should generate the right DDL for a generated column`() {
        val ddl = transaction {
            object : PgTable() {
                override val tableName = "test"

                val id = integer("id")
                val name = text("name")
                val uppercaseName = bool("name_is_short")
                        .generated { name.upperCase().charLength() less 10 }

                override val primaryKey = PrimaryKey(id)
            }.ddl.first().also { println("ddl: $it") }
        }

        assertTrue(ddl.contains("BOOLEAN GENERATED ALWAYS AS ( CHAR_LENGTH(UPPER(test.\"name\")) < 10 ) STORED NOT NULL"))
    }

    @Test fun `should throw an IllegalStateException when making a nullable column generated`() {
        assertThrows(IllegalStateException::class.java) {
            object : PgTable() {
                val id = integer("id")
                val name = text("name")
                val uppercaseName = bool("name_is_short")
                        .nullable()
                        .generated { name.upperCase().charLength() less 10 }

                override val primaryKey = PrimaryKey(id)
            }
        }.also { println("exception : ${it::class.simpleName}: ${it.message}") }
    }

}
