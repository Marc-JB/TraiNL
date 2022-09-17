
import kotlinx.kover.api.CoverageEngine
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed.
@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)
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

fun hasLowerStability(candidateVersion: String, currentVersion: String): Boolean {
    val candidateVersionUpperCased = candidateVersion.toUpperCase()
    val currentVersionUpperCased = currentVersion.toUpperCase()

    val versionsToCheck = mutableListOf<String>()
    when {
        "ALPHA" in currentVersionUpperCased -> {
            // NOTHING
        }
        "BETA" in currentVersionUpperCased -> {
            versionsToCheck += "ALPHA"
        }
        "RC" in currentVersionUpperCased -> {
            versionsToCheck += "ALPHA"
            versionsToCheck += "BETA"
        }
        else -> {
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
