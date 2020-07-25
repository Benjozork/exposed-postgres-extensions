package epgx.test.types

import epgx.test.models.DatabaseConnectedTest
import epgx.types.JsonbColumnType

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class JsonbColumnTest : DatabaseConnectedTest() {

    data class Person (
        val name: String,
        val height: Int
    )

    private val personSerializer: (Person) -> String =
            { person -> "{ \"name\": \"${person.name}\", \"height\": ${person.height} }" }

    private val personDeserializer: (String) -> Person =
            { json ->
                val (_, name, height) = Regex("\\{\\s*\"name\"\\s*:\\s*\"(.*)\",\\s*\"height\"\\s*:\\s*(\\d+)\\s*}").matchEntire(json)!!.groupValues

                Person(name, height.toInt())
            }

    @Test fun `should give the right type string`() {
        assertEquals("JSONB", JsonbColumnType({ it }, { it }).sqlType().also { println("type : $it") })
    }

    @Test fun `should properly output JSON for the database`() {
        val person = Person("Kira", 135)
        val type = JsonbColumnType(personSerializer, personDeserializer)

        assertEquals (
            personSerializer(person),
            type.notNullValueToDB(person)
                    .also { println("notNullValueToDB : $it") }
        )
    }

    @Test fun `should properly accept JSON from the database`() {
        val person = Person("Kira", 135)
        val type = JsonbColumnType(personSerializer, personDeserializer)

        assertEquals (
            person,
            type.valueFromDB(personSerializer(person))
                    .also { println("valueFromDB : $it") }
        )
    }

}
