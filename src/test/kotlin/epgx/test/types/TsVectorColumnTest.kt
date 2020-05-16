package epgx.test.types

import epgx.models.values.TsVector
import epgx.test.models.DatabaseConnectedTest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TsVectorColumnTest : DatabaseConnectedTest() {

    @Test fun `should parse a simple tsvector correctly`() {
        val sourceString = "'I' 'am' 'great' 'hi'"

        val parsedTsVector = TsVector.parse(sourceString)

        println("vector: $sourceString")

        assertEquals("TsVector(Element(lexeme=I, occurrences={}), " +
                              "Element(lexeme=am, occurrences={}), " +
                              "Element(lexeme=great, occurrences={}), " +
                              "Element(lexeme=hi, occurrences={}))", parsedTsVector.toString())
    }

    @Test fun `should parse a tsvector with occurrences correctly`() {
        val sourceString = "'great':4,8 'hi':1,5,6,7 'wha':11,12"

        val parsedTsVector = TsVector.parse(sourceString)

        println("vector: $sourceString")

        assertEquals("TsVector(Element(lexeme=great, occurrences={4=null, 8=null}), " +
                              "Element(lexeme=hi, occurrences={1=null, 5=null, 6=null, 7=null}), " +
                              "Element(lexeme=wha, occurrences={11=null, 12=null}))", parsedTsVector.toString())
    }

    @Test fun `should parse a tsvector with occurrences and weights correctly`() {
        val sourceString = "'bit':11C 'bodi':6C 'hello':4B 'import':13C 'less':12C 'line':3B 'text':7C 'titl':2B"

        val parsedTsVector = TsVector.parse(sourceString)

        println("vector: $sourceString")

        assertEquals("TsVector(Element(lexeme=bit, occurrences={11=C}), " +
                              "Element(lexeme=bodi, occurrences={6=C}), " +
                              "Element(lexeme=hello, occurrences={4=B}), " +
                              "Element(lexeme=import, occurrences={13=C}), " +
                              "Element(lexeme=less, occurrences={12=C}), " +
                              "Element(lexeme=line, occurrences={3=B}), " +
                              "Element(lexeme=text, occurrences={7=C}), " +
                              "Element(lexeme=titl, occurrences={2=B}))", parsedTsVector.toString())
    }

}
