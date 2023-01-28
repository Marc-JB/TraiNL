import kotlinx.kover.api.DefaultIntellijEngine
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Firebase crashlytics
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false

    // Testing
    alias(libs.plugins.kover)
    alias(libs.plugins.sonarqube)

    // API
    alias(libs.plugins.kotlin.serialization)

    // Other
    alias(libs.plugins.versioncheck)
}

val coverageExclusionList = listOf(
    "nl.marc_apps.ovgo.BuildConfig",
    "*.ComposableSingletons",
    "nl.marc_apps.ovgo.ui.preview.fixtures.*PreviewParameterProvider",
    "nl.marc_apps.ovgo.data.db.*_Impl*"
)

kover {
    engine.set(DefaultIntellijEngine)
}

koverMerged {
    enable()

    filters {
        classes {
            excludes += coverageExclusionList
        }
    }
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

sonar {
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
        
        property("sonar.androidLint.reportPaths", "${projectDir.invariantSeparatorsPath}/app/build/reports/lint-results-debug.xml")
    }
}

tasks.sonar {
    dependsOn("lint", "koverMergedReport")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun hasLowerStability(candidateVersion: String, currentVersion: String): Boolean {
    val candidateVersionUpperCased = candidateVersion.toUpperCase()
    val currentVersionUpperCased = currentVersion.toUpperCase()

    val versionsToCheck = mutableListOf<String>()
    when {
        "ALPHA" in currentVersionUpperCased -> {
            // NOTHING
        }
        "BETA" in currentVersionUpperCased -> {
            versionsToCheck += "DEV"
            versionsToCheck += "ALPHA"
        }
        "RC" in currentVersionUpperCased -> {
            versionsToCheck += "DEV"
            versionsToCheck += "ALPHA"
            versionsToCheck += "BETA"
        }
        else -> {
            versionsToCheck += "DEV"
            versionsToCheck += "ALPHA"
            versionsToCheck += "BETA"
            versionsToCheck += "RC"
        }
    }

    return versionsToCheck.any { it in candidateVersionUpperCased }
}

tasks.dependencyUpdates {
    rejectVersionIf {
        hasLowerStability(candidate.version, currentVersion)
    }
}
