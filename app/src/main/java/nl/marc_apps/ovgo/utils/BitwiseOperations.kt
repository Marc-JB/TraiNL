package nl.marc_apps.ovgo.utils

object BitwiseOperations {
    fun constructBooleanInt(vararg booleans: Boolean): Int {
        if (booleans.size > 32) {
            throw IllegalArgumentException("An int can only support up to 32 zeros or ones!")
        }

        var number = 0
        for (bool in booleans) {
            number = number shl 1 or bool.asBit
        }
        return number
    }
}
