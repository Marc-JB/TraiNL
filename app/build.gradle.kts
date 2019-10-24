plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        fun setAppVersion(major: Int, minor: Int, release: Int = 0) {
            versionCode = major * 100 + minor * 10 + release
            versionName = "$major.$minor.$release"
        }

        applicationId = "nl.marc_apps.ovgo"
        minSdkVersion(19)
        targetSdkVersion(29)
        setAppVersion(0, 1, 0)
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
}

kapt.generateStubs = true

dependencies {
    // File tree libraries
    implementation(fileTree(mapOf(
            "include" to listOf("*.jar"),
            "dir" to "libs"
    )))

    // Modules
    implementation(project(":DomainModels"))
    implementation(project(":DomainServices"))
    implementation(project(":API"))

    // General libraries
    implementation(kotlin("stdlib-jdk7", "1.3.50"))

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("com.google.android.material:material:1.1.0-beta01")
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

    implementation("com.google.dagger:dagger:2.25.2")
    implementation("com.google.dagger:dagger-android:2.25.2")
    implementation("com.google.dagger:dagger-android-support:2.25.2")

    kapt("com.google.dagger:dagger-compiler:2.25.2")
    kapt("com.google.dagger:dagger-android-processor:2.25.2")

    // Testing libraries
    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
