@file:Suppress("UnstableApiUsage")

import kotlinx.kover.api.KoverTaskExtension
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)

    // Firebase crashlytics
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)

    // API
    alias(libs.plugins.kotlin.serialization)

    // Testing
    alias(libs.plugins.kover)
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

android {
    compileSdk = libs.versions.trainl.targetsdk.get().toInt()
    buildToolsVersion = "33.0.1"

    namespace = "nl.marc_apps.ovgo"

    packagingOptions {
        resources {
            excludes += "kotlin/**"
            excludes += "**/*.kotlin_metadata"
            excludes += "DebugProbesKt.bin"
            excludes += "META-INF/*.kotlin_module"
            excludes += "build-data.properties"
        }
    }

    val keys = getLocalProperties()

    fun getProperty(key: String): String? {
        return keys.getProperty(key) ?: System.getenv(key.toUpperCaseAsciiOnly().replace(".", "_"))
    }

    defaultConfig {
        applicationId = "nl.marc_apps.ovgo"

        minSdk = libs.versions.trainl.minsdk.get().toInt()
        targetSdk = libs.versions.trainl.targetsdk.get().toInt()

        versionCode = getProperty("version.code")?.toInt() ?: 13
        versionName = getProperty("version.name") ?: "0.10"

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

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-preview"

            isMinifyEnabled = false
            isShrinkResources = false

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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }

        unitTests.all {
            it.extensions.configure<KoverTaskExtension> {
                isDisabled.set(!it.name.contains("debug", ignoreCase = true))
            }
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    // Data
    implementation(libs.bundles.retrofit.extended)
    implementation(libs.kotlin.serialization)

    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)

    // User Interface
    implementation(libs.androidx.navigation)

    implementation(libs.coil)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.androidx.compose.tooling)

    implementation(libs.bundles.google.material)

    // Utilities
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.crashlytics)

    implementation(libs.koin)

    implementation(libs.kotlin.coroutines)
    testImplementation(libs.kotlin.coroutines.test)

    implementation(libs.kotlin.datetime)

    // Compose testing
    testImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.androidx.compose.testing)
    androidTestImplementation(libs.androidx.compose.testing)
    debugImplementation(libs.androidx.compose.testing.manifest)

    // Test utilities
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.robolectric)
    testImplementation(libs.bundles.mockk)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.junit)

    // Android test utilities
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(kotlin("test-junit"))
}
