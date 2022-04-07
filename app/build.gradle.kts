import kotlinx.kover.api.KoverTaskExtension
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.6.20-1.0.4"

    // Firebase crashlytics
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    // Testing
    id("org.sonarqube") version "3.3"

    // Navigation
    id("androidx.navigation.safeargs.kotlin")

    // API
    kotlin("plugin.serialization")
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

android {
    compileSdk = 31
    buildToolsVersion = "32.0.0"

    packagingOptions {
        resources {
            excludes += "kotlin/**"
            excludes += "**/*.kotlin_metadata"
            excludes += "DebugProbesKt.bin"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/*.version"
            excludes += "build-data.properties"
        }
    }

    val keys = getLocalProperties()

    fun getProperty(key: String): String? {
        return keys.getProperty(key) ?: System.getenv(key.toUpperCaseAsciiOnly().replace(".", "_"))
    }

    defaultConfig {
        applicationId = "nl.marc_apps.ovgo"
        minSdk = 21
        targetSdk = 31
        versionCode = getProperty("version.code")?.toInt() ?: 12
        versionName = getProperty("version.name") ?: "0.9"

        testBuildType = "debug"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DUTCH_RAILWAYS_TRAVEL_INFO_API_KEY", "\"${getProperty("dutchRailways.travelInfoApi.key")}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("key.jks")
            storePassword = getProperty("android.store.password")
            keyAlias = getProperty("android.key.alias")
            keyPassword = getProperty("android.key.password")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-preview"

            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    bundle {
        abi.enableSplit = true
        language.enableSplit = true
        density.enableSplit = true
        texture.enableSplit = true
        deviceTier.enableSplit = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
        // compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }

        unitTests.all {
            it.jvmArgs("-noverify")

            it.extensions.configure<KoverTaskExtension> {
                isDisabled = !it.name.contains("debug", ignoreCase = true)

                excludes = coverageExclusionList
            }
        }
    }
}

tasks.koverHtmlReport {
    excludes = coverageExclusionList
}

tasks.koverXmlReport {
    excludes = coverageExclusionList
}

dependencies {
    // Firebase crashlytics
    implementation(platform("com.google.firebase:firebase-bom:29.0.2"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Navigation
    val androidxNavigationVersion = rootProject.extra["androidxNavigationVersion"]
    implementation("androidx.navigation:navigation-fragment-ktx:${androidxNavigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:$androidxNavigationVersion")
    testImplementation("androidx.navigation:navigation-testing:$androidxNavigationVersion")

    // API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-brotli:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-dnsoverhttps:4.9.3")

    // Database
    val roomVersion = "2.4.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Dependency Injection
    val koinVersion = "3.1.5"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-navigation:$koinVersion")
    // testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
    // androidTestImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    // Backward compatibility & utilities
    implementation("androidx.core:core-ktx:1.7.0")

    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("androidx.fragment:fragment-ktx:1.4.1")
    // TODO: Change to testImplementation when https://issuetracker.google.com/issues/127986458 is fixed
    debugImplementation("androidx.fragment:fragment-testing:1.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")

    implementation("io.coil-kt:coil:1.4.0")

    // Design
    implementation("com.google.android.material:material:1.5.0")

    // Test base
    val mockkVersion = "1.12.3"
    testImplementation(kotlin("test-junit"))
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent-jvm:${mockkVersion}")
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.3")

    // Android test base
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation(kotlin("test-junit"))
}

sonarqube {
    val keys = getLocalProperties()

    fun getProperty(key: String): String? {
        return keys.getProperty(key) ?: System.getenv(key.toUpperCaseAsciiOnly().replace(".", "_"))
    }

    properties {
        property("sonar.projectKey", getProperty("sonar.projectKey")!!)
        property("sonar.organization", getProperty("sonar.organization")!!)
        property("sonar.host.url", getProperty("sonar.host.url")!!)

        property("sonar.coverage.jacoco.xmlReportPaths", "**/build/reports/kover/project-xml/report.xml")
    }
}
