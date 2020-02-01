plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Modules
    implementation(project(":domain"))

    // Other
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    implementation("com.squareup.okhttp3:okhttp:4.3.1")
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
}