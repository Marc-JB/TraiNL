plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Other
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
}