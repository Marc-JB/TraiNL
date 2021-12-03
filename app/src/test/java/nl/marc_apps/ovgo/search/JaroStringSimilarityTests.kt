package nl.marc_apps.ovgo.search

import kotlin.test.*

class JaroStringSimilarityTests {
    @Test
    fun similarityIsZeroWhenFirstStringIsEmpty() {
        // Arrange
        val firstString = ""
        val secondString = "ABC"

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun similarityIsZeroWhenSecondStringIsEmpty() {
        // Arrange
        val firstString = "ABC"
        val secondString = ""

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun similarityIsOneThousandWhenBothStringsAreEqual() {
        // Arrange
        val firstString = "ABCDEF"
        val secondString = "ABCDEF"

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(1000, result)
    }

    @Test
    fun similarityIsOneThousandWhenBothStringsAreEmpty() {
        // Arrange
        val firstString = ""
        val secondString = ""

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(1000, result)
    }

    @Test
    fun testWordSimilarity1() {
        // Arrange
        val firstString = "CRATE"
        val secondString = "TRACE"

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(733, result)
    }

    @Test
    fun testWordSimilarity2() {
        // Arrange
        val firstString = "DwAyNE"
        val secondString = "DuANE"

        // Act
        val result = JaroStringSimilarity.calculateSimilarity(firstString, secondString)

        // Assert
        assertEquals(822, result)
    }
}
