package nl.marc_apps.ovgo.search

/**
 * @see "https://www.geeksforgeeks.org/jaro-and-jaro-winkler-similarity/"
 */
object JaroStringSimilarity : StringSimilarity {
    private const val THREE_NUMBERS_AVERAGE_BY_DIVISION = 3

    override fun calculateSimilarity(firstString: String, secondString: String): Int {
        if (firstString == secondString) {
            return StringSimilarity.EXACT_MATCH
        }

        if (firstString.isEmpty() || secondString.isEmpty()) {
            return StringSimilarity.NO_MATCH
        }

        val maxMatchingDistance = maxOf(firstString.length, secondString.length) / 2 - 1

        var matchCount = 0

        val firstStringMatches = BooleanArray(firstString.length)
        val secondStringMatches = BooleanArray(secondString.length)

        for (i in firstString.indices) {
            val low = maxOf(0, i - maxMatchingDistance)
            val high = minOf(secondString.length - 1, i + maxMatchingDistance)
            for (j in low..high) {
                if (firstString.getOrNull(i) == secondString.getOrNull(j) && !secondStringMatches[j]) {
                    firstStringMatches[i] = true
                    secondStringMatches[j] = true
                    matchCount++
                    break
                }
            }
        }

        if (matchCount == 0) {
            return StringSimilarity.NO_MATCH
        }

        val transpositionCount = calculateTranspositionCount(firstString, secondString, firstStringMatches, secondStringMatches)

        val firstStringWeight = matchCount * StringSimilarity.EXACT_MATCH / firstString.length
        val secondStringWeight = matchCount * StringSimilarity.EXACT_MATCH / secondString.length
        val transpositionWeight = (matchCount * 2 - transpositionCount) * (StringSimilarity.EXACT_MATCH / 2) / matchCount
        return (firstStringWeight + secondStringWeight + transpositionWeight) / THREE_NUMBERS_AVERAGE_BY_DIVISION
    }

    private fun calculateTranspositionCount(
        firstString: String,
        secondString: String,
        firstStringMatches: BooleanArray,
        secondStringMatches: BooleanArray
    ): Int {
        var transpositionCount = 0

        var points = 0

        for (i in firstString.indices) {
            if (firstStringMatches[i]) {
                while (!secondStringMatches[points]) {
                    points++
                }

                if (firstString.getOrNull(i) != secondString.getOrNull(points++)) {
                    transpositionCount++
                }
            }
        }

        return transpositionCount
    }
}
