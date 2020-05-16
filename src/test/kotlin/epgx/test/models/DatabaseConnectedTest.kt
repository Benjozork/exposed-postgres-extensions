package epgx.test.models

import epgx.models.PgTable

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.*

@ExtendWith(DatabaseConnectedTest::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class DatabaseConnectedTest(private vararg val tables: PgTable = arrayOf())
    : BeforeEachCallback, AfterEachCallback {

    @BeforeAll fun setUpDatabase() {
        val dbUrl = System.getenv("EPGX_TEST_DB_URL") ?: error("EPGX_TEST_DB_URL not set")

        val dbUser = System.getenv("EPGX_TEST_DB_USER") ?: error("EPGX_TEST_DB_USER not set")

        val dbPassword = System.getenv("EPGX_TEST_DB_PASSWORD") ?: error("EPGX_TEST_DB_PASSWORD not set")

        Database.connect(dbUrl, "org.postgresql.Driver", dbUser, dbPassword)

        transaction { SchemaUtils.drop(*tables) }
        transaction { SchemaUtils.create(*tables) }
    }

    override fun beforeEach(context: ExtensionContext?) {
        val name = context?.testMethod?.get()?.name ?: error("test method is null")

        println("=== [ Test : $name ] ===\n")
    }

    override fun afterEach(context: ExtensionContext?) = println()

    @AfterAll fun tearDownTables() {
        transaction { SchemaUtils.drop(*tables) }
    }

}
