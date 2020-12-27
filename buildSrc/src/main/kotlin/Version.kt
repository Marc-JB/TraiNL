data class Version(val major: Int, val minor: Int, val patch: Int = 0) {
    val code = major * 100 + minor * 10 + patch
    val name = "$major.$minor.$patch"

    infix fun flavor(code: Int) = this.code * 10 + code

    override fun toString() = name
}
