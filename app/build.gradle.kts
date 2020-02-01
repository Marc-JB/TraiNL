plugins {
    id("com.android.application")
    kotlin("android")
}

val minSdk = 23
val targetAndCompileSdk = 29
val appVersion = Triple(0, 1, 0)

android {
    compileSdkVersion(targetAndCompileSdk)
    buildToolsVersion = "29.0.2"

    defaultConfig {
        applicationId = "nl.marc_apps.ovgo"

        minSdkVersion(minSdk)
        targetSdkVersion(targetAndCompileSdk)

        val (major, minor, release) = appVersion
        versionCode = major * 100 + minor * 10 + release
        versionName = "$major.$minor.$release"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
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

    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-scope:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")

    // Testing libraries
    testImplementation("junit:junit:4.13")

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
