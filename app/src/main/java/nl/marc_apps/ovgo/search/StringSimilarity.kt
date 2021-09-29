package nl.marc_apps.ovgo.search

interface StringSimilarity {
    fun calculateSimilarity(firstString: String, secondString: String): Double
}
