package nl.marc_apps.ovgo.utils

import kotlin.test.*

class BitwiseOperationsUtilsTests {
    class BooleanArrayToIntTests {
        @Test
        fun testBooleanArrayToInt() {
            // Arrange
            val booleanInput = booleanArrayOf(true, false, true, true, false, false, false, true, true, true)
            val intOutput = 0b1110001101

            // Act
            val actualOutput = BitwiseOperations.constructBooleanInt(*booleanInput)

            // Assert
            assertEquals(intOutput, actualOutput)
        }

        @Test
        fun testBooleanArrayToIntWithLeadingFalse() {
            // Arrange
            val booleanInput = booleanArrayOf(false, true, false, false, true, true, false)
            val intOutput = 0b110010

            // Act
            val actualOutput = BitwiseOperations.constructBooleanInt(*booleanInput)

            // Assert
            assertEquals(intOutput, actualOutput)
        }

        @Test
        fun booleanArrayToIntShouldThrowWhenInputIsLargerThanBitSize() {
            // Arrange
            val booleanInput = BooleanArray(Int.SIZE_BITS + 1) {
                it == 0 || booleanArrayOf(true, false).random()
            }

            // Act & Assert
            assertFailsWith<IllegalArgumentException> {
                BitwiseOperations.constructBooleanInt(*booleanInput)
            }
        }
    }

    class BooleanFromIntTests {
        @Test
        fun booleanIndexOutOfBoundShouldReturnFalse() {
            // Arrange
            val intInput = 0b0101
            val index = 6

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertFalse(output)
        }

        @Test
        fun singleTruthyBitShouldReturnTrue() {
            // Arrange
            val intInput = 0b1
            val index = 0

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertTrue(output)
        }

        @Test
        fun singleFalsyBitShouldReturnFalse() {
            // Arrange
            val intInput = 0b0
            val index = 0

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertFalse(output)
        }

        @Test
        fun doubleTruthyBitShouldReturnTrueForFirstIndex() {
            // Arrange
            val intInput = 0b11
            val index = 0

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertTrue(output)
        }

        @Test
        fun doubleTruthyBitShouldReturnTrueForLastIndex() {
            // Arrange
            val intInput = 0b11
            val index = 1

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertTrue(output)
        }

        @Test
        fun truthyBitSuffixedByFalsyBitShouldReturnTrueForFirstIndex() {
            // Arrange
            val intInput = 0b10
            val index = 1

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertTrue(output)
        }

        @Test
        fun truthyBitSuffixedByFalsyBitShouldReturnFalseForLastIndex() {
            // Arrange
            val intInput = 0b10
            val index = 0

            // Act
            val output = BitwiseOperations.getBooleanFromInt(intInput, index)

            // Assert
            assertFalse(output)
        }
    }

    @Test
    fun randomlyConstructedBooleanArraysConvertedToIntAndBackShouldBeEqualToOriginal() {
        val testRuns = 20
        for (i in 0 until testRuns) {
            // Arrange
            val arraySize = Int.SIZE_BITS
            val booleanInput = BooleanArray(arraySize) { booleanArrayOf(true, false).random() }

            // Act
            val intOutput = BitwiseOperations.constructBooleanInt(*booleanInput)
            val booleanOutput = BooleanArray(arraySize) {
                BitwiseOperations.getBooleanFromInt(intOutput, it)
            }

            // Assert
            assertContentEquals(booleanInput, booleanOutput)
        }
    }
}
