package nl.marc_apps.ovgo.search

import kotlin.math.floor

/**
 * @see "https://www.geeksforgeeks.org/jaro-and-jaro-winkler-similarity/"
 */
object JaroStringSimilarity : StringSimilarity {
    private const val NO_MATCHES = 0.0

    private const val EXACT_MATCH = 1.0

    private const val TWO_NUMBERS_AVERAGE_BY_DIVISION = 2.0

    private const val THREE_NUMBERS_AVERAGE_BY_DIVISION = 3.0

    override fun calculateSimilarity(firstString: String, secondString: String): Double {
        if (firstString == secondString) {
            return EXACT_MATCH
        }

        if (firstString.isEmpty() || secondString.isEmpty()) {
            return NO_MATCHES
        }

        val maxMatchingDistance = floor(maxOf(firstString.length, secondString.length) / TWO_NUMBERS_AVERAGE_BY_DIVISION).toInt() - 1

        var matchCount = 0

        val firstStringMatches = BooleanArray(firstString.length)
        val secondStringMatches = BooleanArray(secondString.length)

        for (i in firstString.indices) {
            val low = maxOf(0, i - maxMatchingDistance)
            val high = minOf(secondString.length, i + maxMatchingDistance + 1)
            for (j in low until high) {
                if (firstString.getOrNull(i) == secondString.getOrNull(j) && !secondStringMatches[j]) {
                    firstStringMatches[i] = true
                    secondStringMatches[j] = true
                    matchCount++
                    break
                }
            }
        }

        if (matchCount == 0) {
            return NO_MATCHES
        }

        val transpositionCount = calculateTranspositionCount(firstString, secondString, firstStringMatches, secondStringMatches)

        val firstStringWeight = matchCount / firstString.length.toDouble()
        val secondStringWeight = matchCount / secondString.length.toDouble()
        val transpositionWeight = (matchCount - transpositionCount) / matchCount
        return (firstStringWeight + secondStringWeight + transpositionWeight) / THREE_NUMBERS_AVERAGE_BY_DIVISION
    }

    private fun calculateTranspositionCount(
        firstString: String,
        secondString: String,
        firstStringMatches: BooleanArray,
        secondStringMatches: BooleanArray
    ): Double {
        var transpositionCount = 0.0

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

        return transpositionCount / 2.0
    }
}
