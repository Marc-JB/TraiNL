import kotlinx.kover.api.CoverageEngine
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly
import org.jetbrains.kotlin.konan.properties.Properties

val androidxNavigationVersion by extra("2.5.0")

plugins {
    val kotlinVersion = "1.7.10"

    id("com.android.application") version "7.2.1" apply false
    kotlin("android") version kotlinVersion apply false

    // Firebase crashlytics
    id("com.google.gms.google-services") version "4.3.13" apply false
    id("com.google.firebase.crashlytics") version "2.9.1" apply false

    // Navigation
    id("androidx.navigation.safeargs.kotlin") version "2.5.0" apply false

    // Testing
    id("org.jetbrains.kotlinx.kover") version "0.5.1"
    id("org.sonarqube") version "3.3"

    // API
    kotlin("plugin.serialization") version kotlinVersion apply false
}

val coverageExclusionList = listOf(
    "nl.marc_apps.ovgo.databinding.*",
    "nl.marc_apps.ovgo.BuildConfig",
    "nl.marc_apps.ovgo.data.db.*_Impl",
    "nl.marc_apps.ovgo.data.db.*_Impl*",
    "nl.marc_apps.ovgo.ui.*.*FragmentArgs",
    "nl.marc_apps.ovgo.ui.*.*FragmentArgs*",
    "nl.marc_apps.ovgo.ui.*.*FragmentDirections",
    "nl.marc_apps.ovgo.ui.*.*FragmentDirections*"
)

kover {
    coverageEngine.set(CoverageEngine.INTELLIJ)
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

sonarqube {
    val keys = getLocalProperties()

    fun getProperty(key: String): String? {
        return keys.getProperty(key) ?: System.getenv(key.toUpperCaseAsciiOnly().replace(".", "_"))
    }

    properties {
        property("sonar.projectName", "TraiNL")

        property("sonar.projectKey", getProperty("sonar.projectKey")!!)
        property("sonar.organization", getProperty("sonar.organization")!!)
        property("sonar.host.url", getProperty("sonar.host.url")!!)

        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir.invariantSeparatorsPath}/build/reports/kover/report.xml")
    }
}

tasks.sonarqube {
    dependsOn("koverMergedReport")
}

tasks.koverMergedHtmlReport {
    excludes = coverageExclusionList
}

tasks.koverMergedXmlReport {
    excludes = coverageExclusionList
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
