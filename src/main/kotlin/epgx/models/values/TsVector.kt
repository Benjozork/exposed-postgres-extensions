package epgx.models.values

/**
 * Represents a [tsvector](https://www.postgresql.org/docs/current/datatype-textsearch.html) object.
 *
 * @author Benjozork
 *
 * @property elements an array of [Element] objects composing the `tsvector`.
 */
data class TsVector (
    val elements: Array<Element>
) {
    /**
     * Represents an entry in a [TsVector].
     *
     * @property lexeme      the lexeme this entry represents
     * @property occurrences an optional map of occurrences to optional [weights][Weight]
     */
    data class Element (
        val lexeme: String,
        val occurrences: Map<Int, Weight?>?
    ) {

        /**
         * The weight of an occurrence of a lexeme
         */
        enum class Weight { A, B, C, D }

        companion object {
            fun parse(lexeme: String, occurrencesString: String?): Element {
                val occurences = occurrencesString?.split(',')?.filter(String::isNotEmpty)?.map {
                    val match = Regex("(\\d+)([ABCD])?").matchEntire(it)!!.groupValues

                    match[1].toInt() to try { Weight.valueOf(match[2]) } catch (_: Exception) { null }
                }?.toMap() ?: emptyMap()

                return Element(lexeme, occurences)
            }
        }
    }

    override fun toString() = "TsVector(${elements.joinToString()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TsVector) return false

        if (!elements.contentEquals(other.elements)) return false

        return true
    }

    override fun hashCode(): Int {
        return elements.contentHashCode()
    }

    companion object {
        fun parse(text: String): TsVector {
            val vectorElementsRegex = Regex("'(\\w+)'(?::((?:\\w+,?)+)+)?")

            val matches = vectorElementsRegex.findAll(text)
                .toList()
                .map { it.groupValues.let { g -> g[1] to g[2] } }

            return TsVector(matches.map { Element.parse(it.first, it.second) }.toTypedArray())
        }
    }
}
