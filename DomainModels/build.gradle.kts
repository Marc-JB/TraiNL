plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf(
            "include" to listOf("*.jar"),
            "dir" to "libs"
    )))

    // Other
    implementation(kotlin("stdlib-jdk7", "1.3.31"))
}