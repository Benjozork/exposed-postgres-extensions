package epgx.types

import epgx.models.values.TsVector

import org.jetbrains.exposed.sql.ColumnType

/**
 * Column type for storing [TsVector] objects.
 *
 * @author Benjozork
 */
class TsVectorColumnType : ColumnType() {

    override fun sqlType() = "TSVECTOR"

    override fun valueFromDB(value: Any) = TsVector.parse(value.toString())

}
