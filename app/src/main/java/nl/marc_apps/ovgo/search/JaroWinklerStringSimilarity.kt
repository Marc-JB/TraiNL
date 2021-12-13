package nl.marc_apps.ovgo.search

/**
 * @see "https://www.geeksforgeeks.org/jaro-and-jaro-winkler-similarity/"
 */
object JaroWinklerStringSimilarity : StringSimilarity {
    private const val WEIGHT_THRESHOLD = StringSimilarity.EXACT_MATCH * 7 / 10

    private const val PREFIX_MAX_CHARACTER_COUNT = 4

    private const val PREFIX_WEIGHT = 10

    override fun calculateSimilarity(firstString: String, secondString: String): Int {
        val jaroStringSimilarity = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        if (jaroStringSimilarity > WEIGHT_THRESHOLD) {
            var matchedPrefixLength = 0
            for (i in 0 until minOf(firstString.length, secondString.length, PREFIX_MAX_CHARACTER_COUNT)) {
                if (firstString.getOrNull(i) == secondString.getOrNull(i)) {
                    matchedPrefixLength++
                } else {
                    break
                }
            }

            val winklerStringSimilarity = matchedPrefixLength * (StringSimilarity.EXACT_MATCH - jaroStringSimilarity)
            val jaroWinklerStringSimilarity = jaroStringSimilarity + winklerStringSimilarity / PREFIX_WEIGHT

            return if (jaroWinklerStringSimilarity > StringSimilarity.EXACT_MATCH) {
                StringSimilarity.EXACT_MATCH
            } else {
                jaroWinklerStringSimilarity
            }
        }

        return jaroStringSimilarity
    }
}
