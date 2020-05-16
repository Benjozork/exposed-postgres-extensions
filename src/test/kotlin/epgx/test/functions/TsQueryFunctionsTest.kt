package epgx.test.functions

import epgx.functions.phraseToTsQuery
import epgx.functions.plainToTsQuery
import epgx.functions.toTsQuery
import epgx.functions.webSearchToTsQuery
import epgx.models.PgTable
import epgx.test.models.DatabaseConnectedTest

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class TsQueryFunctionsTest : DatabaseConnectedTest(TsQueryTestTable) {

    private object TsQueryTestTable : PgTable() {
        val id = integer("id").autoIncrement()
        val text = text("text")

        override val primaryKey = PrimaryKey(id)
    }

    @BeforeEach fun resetData() {
        transaction {
            TsQueryTestTable.deleteAll()
        }
    }

    @Test fun `should execute toTsQuery() function properly on the postgres docs data`() {
        transaction {
            TsQueryTestTable.insert { it[text] = "The & Fat & Rats" }
            TsQueryTestTable.insert { it[text] = "Fat | Rats:AB" }
            TsQueryTestTable.insert { it[text] = "supern:*A & star:A*B" }
            TsQueryTestTable.insert { it[text] = "'supernovae stars' & !crab" }
        }

        val results = transaction {
            TsQueryTestTable.slice(TsQueryTestTable.text.toTsQuery("english")).selectAll()
                .map { "${it[it.fieldIndex.keys.first()]}" }.also { println(it.joinToString("\n")) }
        }

        assertTrue(results.contains("'fat' & 'rat'"))
        assertTrue(results.contains("'fat' | 'rat':AB"))
        assertTrue(results.contains("'supern':*A & 'star':*AB"))
        assertTrue(results.contains("'supernova' & 'star' & !'crab'"))
    }

    @Test fun `should execute plainToTsQuery() function properly on the postgres docs data`() {
        transaction {
            TsQueryTestTable.insert { it[text] = "The Fat Rats" }
            TsQueryTestTable.insert { it[text] = "The Fat & Rats:C" }
        }

        val results = transaction {
            TsQueryTestTable.slice(TsQueryTestTable.text.plainToTsQuery("english")).selectAll()
                .map { "${it[it.fieldIndex.keys.first()]}" }.also { println(it.joinToString("\n")) }
        }

        assertTrue(results.contains("'fat' & 'rat'"))
        assertTrue(results.contains("'fat' & 'rat' & 'c'"))
    }

    @Test fun `should execute phraseToTsQuery() function properly on the postgres docs data`() {
        transaction {
            TsQueryTestTable.insert { it[text] = "The Fat Rats" }
            TsQueryTestTable.insert { it[text] = "The Fat & Rats:C" }
        }

        val results = transaction {
            TsQueryTestTable.slice(TsQueryTestTable.text.phraseToTsQuery("english")).selectAll()
                .map { "${it[it.fieldIndex.keys.first()]}" }.also { println(it.joinToString("\n")) }
        }

        assertTrue(results.contains("'fat' <-> 'rat'"))
        assertTrue(results.contains("'fat' <-> 'rat' <-> 'c'"))
    }

    @Test fun `should execute webSearchToTsQuery() function properly on the postgres docs data`() {
        transaction {
            TsQueryTestTable.insert { it[text] = "The Fat Rats" }
            TsQueryTestTable.insert { it[text] = "\"supernovae stars\" -crab" }
            TsQueryTestTable.insert { it[text] = "\"sad cat\" or \"fat rat" }
            TsQueryTestTable.insert { it[text] = "signal -\"segmentation fault\"" }
            TsQueryTestTable.insert { it[text] = "\"\"\" )( dummy \\\\ query <->" }
        }

        val results = transaction {
            TsQueryTestTable.slice(TsQueryTestTable.text.webSearchToTsQuery("english")).selectAll()
                .map { "${it[it.fieldIndex.keys.first()]}" }.also { println(it.joinToString("\n")) }
        }

        assertTrue(results.contains("'fat' & 'rat'"))
        assertTrue(results.contains("'supernova' <-> 'star' & !'crab'"))
        assertTrue(results.contains("'sad' <-> 'cat' | 'fat' & 'rat'"))
        assertTrue(results.contains("'signal' & !( 'segment' <-> 'fault' )"))
        assertTrue(results.contains("'dummi' & 'queri'"))
    }

}
