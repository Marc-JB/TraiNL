plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

val minSdk = 21
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
        vectorDrawables.useSupportLibrary = true
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
}

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf(
            "include" to listOf("*.jar"),
            "dir" to "libs"
    )))

    // Modules
    implementation(project(":domainModels"))
    implementation(project(":domainServices"))
    implementation(project(":api"))
    implementation(project(":ui"))

    // General libraries
    implementation(kotlin("stdlib-jdk8", "1.3.50"))

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("com.google.android.material:material:1.1.0-beta02")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.1.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.1.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-scope:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")

    // Testing libraries
    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
