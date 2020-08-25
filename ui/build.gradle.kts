@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
}

val minSdk = 21
val targetAndCompileSdk = 29

data class Version(val major: Int, val minor: Int, val patch: Int = 0) {
    val code = major * 100 + minor * 10 + patch
    val name = "$major.$minor.$patch"

    override fun toString() = name
}

val libVersion = Version(0, 1)

android {
    compileSdkVersion(targetAndCompileSdk)

    defaultConfig {
        minSdkVersion(minSdk)
        targetSdkVersion(targetAndCompileSdk)

        versionCode = libVersion.code
        versionName = libVersion.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            isZipAlignEnabled = true
            isShrinkResources = false
            isCrunchPngs = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        val debug by getting {
            isMinifyEnabled = false
            isZipAlignEnabled = true
            isShrinkResources = false
            isCrunchPngs = true
        }
    }

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

    // General libraries
    implementation(kotlin("stdlib-jdk8"))

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.1")

    implementation("com.google.android.material:material:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-scope:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")

    // Testing libraries
    testImplementation(kotlin("test-junit"))

    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
}
