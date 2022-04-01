import kotlinx.kover.api.CoverageEngine
import kotlinx.kover.api.KoverExtension

val androidxNavigationVersion by extra("2.4.0-rc01")

plugins {
    val kotlinVersion = "1.6.10"

    id("com.android.application") version "7.1.0" apply false
    kotlin("android") version kotlinVersion apply false

    // Firebase crashlytics
    id("com.google.gms.google-services") version "4.3.10" apply false
    id("com.google.firebase.crashlytics") version "2.8.1" apply false

    // Navigation
    id("androidx.navigation.safeargs.kotlin") version "2.5.0-SNAPSHOT" apply false

    // Testing
    id("org.jetbrains.kotlinx.kover") version "0.5.0-RC2"

    // API
    kotlin("plugin.serialization") version kotlinVersion apply false
}

extensions.configure<KoverExtension> {
    coverageEngine.set(CoverageEngine.INTELLIJ)
    instrumentAndroidPackage = true
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
