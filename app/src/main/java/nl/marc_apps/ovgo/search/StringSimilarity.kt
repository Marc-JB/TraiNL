package nl.marc_apps.ovgo.search

interface StringSimilarity {
    fun calculateSimilarity(firstString: String, secondString: String): Int

    companion object {
        const val EXACT_MATCH = 1000

        const val NO_MATCH = 0
    }
}
