import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"

    // Firebase crashlytics
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    // Testing
    id("de.mannodermaus.android-junit5")

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

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"

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
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

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
}

dependencies {
    // Firebase crashlytics
    implementation(platform("com.google.firebase:firebase-bom:29.0.2"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Navigation
    val androidxNavigationVersion = rootProject.extra["androidxNavigationVersion"]
    implementation("androidx.navigation:navigation-fragment-ktx:${androidxNavigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:$androidxNavigationVersion")

    // API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-brotli:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-dnsoverhttps:4.9.3")

    // Database
    val roomVersion = "2.4.1"
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
    implementation("androidx.fragment:fragment-ktx:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.coil-kt:coil:1.4.0")

    // Design
    implementation("com.google.android.material:material:1.4.0")

    // Test base
    val junitVersion = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // Android test base
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    androidTestImplementation(kotlin("test-junit5"))
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.3.0")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.3.0")
}
