package nl.marc_apps.ovgo.search

import kotlin.test.Test
import kotlin.test.assertEquals

class JaroWinklerStringSimilarityTests {
    @Test
    fun similarityIsZeroWhenFirstStringIsEmpty() {
        // Arrange
        val firstString = ""
        val secondString = "ABC"

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun similarityIsZeroWhenSecondStringIsEmpty() {
        // Arrange
        val firstString = "ABC"
        val secondString = ""

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun similarityIsOneThousandWhenBothStringsAreEqual() {
        // Arrange
        val firstString = "ABCDEF"
        val secondString = "ABCDEF"

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(1000, result)
    }

    @Test
    fun similarityIsOneThousandWhenBothStringsAreEmpty() {
        // Arrange
        val firstString = ""
        val secondString = ""

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(1000, result)
    }

    @Test
    fun testWordSimilarity1() {
        // Arrange
        val firstString = "TRATE"
        val secondString = "TRACE"

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(906, result)
    }

    @Test
    fun testWordSimilarity2() {
        // Arrange
        val firstString = "DwAyNE"
        val secondString = "DuANE"

        // Act
        val result = JaroWinklerStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(839, result)
    }
}
