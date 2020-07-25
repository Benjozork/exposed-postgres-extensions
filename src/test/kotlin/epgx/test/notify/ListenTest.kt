package epgx.test.notify

import epgx.notify.extensions.listen
import epgx.notify.extensions.notify
import epgx.test.models.DatabaseConnectedTest

import org.jetbrains.exposed.sql.transactions.transaction

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ListenTest : DatabaseConnectedTest() {

    @Test fun `should receive messages from channel being listened to`() {
        val receivedMessages = mutableListOf<String>()

        transaction {
            listen("virtual") { _, _, payload -> receivedMessages += payload }

            notify("virtual", "hello")
            notify("virtual", "hi")
            notify("other", "message")
        }

        assertEquals(2, receivedMessages.size)
        assertEquals("hello", receivedMessages[0])
        assertEquals("hi", receivedMessages[1])
    }

}
