import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val androidxNavigationVersion by extra("2.3.5")
    val kotlinVersion = "1.6.10"

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // TODO: Remove manual R8 declaration when AGP 7.1 releases: https://issuetracker.google.com/issues/206855609
        maven(url = "https://storage.googleapis.com/r8-releases/raw")
    }

    dependencies {
        // TODO: Remove manual R8 declaration when AGP 7.1 releases: https://issuetracker.google.com/issues/206855609
        classpath("com.android.tools:r8:3.1.42")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath(kotlin("gradle-plugin", kotlinVersion))

        // Firebase crashlytics
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")

        // Testing
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1")
        classpath("org.jetbrains.kotlinx:kover:0.5.0-RC2")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")

        // Navigation
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$androidxNavigationVersion")

        // API
        classpath(kotlin("serialization", kotlinVersion))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

apply(plugin = "kover")
apply(plugin = "org.sonarqube")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun getLocalProperties(): Properties {
    return Properties().also { properties ->
        try {
            file("../local.properties").inputStream().use {
                properties.load(it)
            }
        } catch (ignored: java.io.FileNotFoundException) {}
    }
}

tasks.getByName("sonarqube", org.sonarqube.gradle.SonarQubeTask::class) {
    val keys = getLocalProperties()

    fun getProperty(key: String): String? {
        return keys.getProperty(key) ?: System.getenv(key.toUpperCaseAsciiOnly().replace(".", "_"))
    }

    setProperty("sonar.projectKey", getProperty("sonar.projectKey"))
    setProperty("sonar.organization", getProperty("sonar.organization"))
    setProperty("sonar.host.url", getProperty("sonar.host.url"))

    setProperty("sonar.coverage.jacoco.xmlReportPaths", "**/build/reports/kover/project-xml/report.xml")
}
