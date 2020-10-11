plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Modules
    implementation(project(":domain"))

    // Other
    `kotlin-stdlib`

    `kotlin-coroutines`
    `kotlin-json-serialization`

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
}
