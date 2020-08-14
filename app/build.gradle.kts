@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("com.android.application")
    kotlin("android")
}

val minSdk = 23
val targetAndCompileSdk = 29

data class Version(val major: Int, val minor: Int, val patch: Int = 0) {
    val code = major * 100 + minor * 10 + patch
    val name = "$major.$minor.$patch"
    override fun toString() = name
}

val appVersion = Version(0, 1, 0)

android {
    compileSdkVersion(targetAndCompileSdk)

    packagingOptions {
        exclude("kotlin/**")
        exclude("**/*.kotlin_metadata")
        exclude("DebugProbesKt.bin")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
        exclude("build-data.properties")
    }

    defaultConfig {
        applicationId = "nl.marc_apps.ovgo"

        minSdkVersion(minSdk)
        targetSdkVersion(targetAndCompileSdk)

        val (major, minor, release) = appVersion
        versionCode = appVersion.code
        versionName = appVersion.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            isZipAlignEnabled = true
            isShrinkResources = false
            isCrunchPngs = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    dataBinding.isEnabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Modules
    implementation(project(":domain"))
    implementation(project(":api"))
    implementation(project(":ui"))

    // General libraries
    implementation(kotlin("stdlib-jdk8"))

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-scope:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")
}
