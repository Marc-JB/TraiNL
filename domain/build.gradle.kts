plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Other
    `kotlin-stdlib`
    `kotlin-json-serialization`
}