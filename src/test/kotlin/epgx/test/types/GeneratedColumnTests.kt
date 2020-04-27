package epgx.test.types

import epgx.models.PgTable
import epgx.functions.charLength

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeneratedColumnTests {

    @BeforeAll fun setup() {
        val dbUrl = System.getenv("EPGX_TEST_DB_URL")
            ?: error("EPGX_TEST_DB_URL not set")

        val dbUser = System.getenv("EPGX_TEST_DB_USER")
            ?: error("EPGX_TEST_DB_USER not set")

        val dbPassword = System.getenv("EPGX_TEST_DB_PASSWORD")
            ?: error("EPGX_TEST_DB_PASSWORD not set")

        Database.connect(dbUrl, "org.postgresql.Driver", dbUser, dbPassword)
    }

    @Test fun `should generate the right DDL for a generated column`() {
        val ddl = transaction {
            object : PgTable() {
                override val tableName = "test"

                val id = integer("id")
                val name = text("name")
                val uppercaseName = bool("name_is_short")
                        .generated { name.upperCase().charLength() less 10 }

                override val primaryKey = PrimaryKey(id)
            }.ddl.also(::println)
        }

        assertTrue(ddl.first().contains("BOOLEAN GENERATED ALWAYS AS ( CHAR_LENGTH(UPPER(test.\"name\")) < 10 ) STORED NOT NULL"))
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
        }
    }

}
