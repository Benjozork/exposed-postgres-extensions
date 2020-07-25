package epgx.notify.extensions

import epgx.notify.ListenStatement
import epgx.notify.NotifyStatement

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.stringLiteral

/**
 * Executes a [`LISTEN`](https://www.postgresql.org/docs/current/sql-listen.html) statement in the [Transaction] on
 * channel [channelName].
 *
 * **Note:** notifications stop being handled when the connection is closed. This should be called in a separate thread
 * that keeps the connection open.
 *
 * @param channelName    the name of the `NOTIFY` channel name
 * @param onNotification the function to call on each received notification
 *
 * @author Benjozork
 */
fun Transaction.listen(channelName: String, onNotification: (pid: Int, channel: String, payload: String) -> Unit): Unit? =
    ListenStatement(channelName, onNotification).execute(this)

/**
 * Executes a [`NOTIFY`](https://www.postgresql.org/docs/current/sql-notify.html) statement in the [Transaction] on
 * channel [channelName].
 *
 * @param channelName the name of the `NOTIFY` channel name
 * @param payload     the payload to send
 *
 * @author Benjozork
 */
fun Transaction.notify(channelName: String, payload: String): Unit? =
    NotifyStatement(channelName, stringLiteral(payload)).execute(this)
