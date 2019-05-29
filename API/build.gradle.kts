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

    // Modules
    implementation(project(":DomainModels"))
    implementation(project(":DomainServices"))

    // Other
    implementation(kotlin("stdlib-jdk7", "1.3.31"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("ru.gildor.coroutines:kotlin-coroutines-retrofit:1.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
}