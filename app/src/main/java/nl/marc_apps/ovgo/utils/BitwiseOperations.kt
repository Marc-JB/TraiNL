package nl.marc_apps.ovgo.utils

object BitwiseOperations {
    fun constructBooleanInt(vararg booleans: Boolean): Int {
        if (booleans.size > Int.SIZE_BITS) {
            throw IllegalArgumentException("An int can only support up to ${Int.SIZE_BITS} zeros or ones!")
        }

        var number = 0
        for (bool in booleans.reversed()) {
            number = number shl 1 or bool.asBit
        }
        return number
    }

    fun getBooleanFromInt(booleanStore: Int, position: Int): Boolean {
        return if (position == 0) {
            booleanStore and 1 == true.asBit
        } else {
            booleanStore shr position and 1 == true.asBit
        }
    }
}
