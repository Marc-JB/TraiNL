plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
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

class ImplementationContext(val block: (dep: String) -> Unit) {
    infix fun String.version(version: String) = block("${this}:$version")

    inline fun group(name: String, b: ImplementationContext.() -> Unit) = b (ImplementationContext { dep -> block ("$name:$dep") })
}

inline fun DependencyHandlerScope.implementations(block: ImplementationContext.() -> Unit) = block(ImplementationContext { dep -> implementation(dep)})

inline fun DependencyHandlerScope.testImplementations(block: ImplementationContext.() -> Unit) = block(ImplementationContext {dep -> testImplementation(dep)})

inline fun DependencyHandlerScope.androidTestImplementations(block: ImplementationContext.() -> Unit) = block(ImplementationContext {dep -> androidTestImplementation(dep)})

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
    implementations {
        "androidx.appcompat:appcompat" version "1.1.0"
        "androidx.core:core-ktx" version "1.1.0"
        "com.google.android.material:material" version "1.1.0-alpha10"
        "androidx.constraintlayout:constraintlayout" version "1.1.3"
        "androidx.vectordrawable:vectordrawable" version "1.1.0"
        "androidx.legacy:legacy-support-v4" version "1.0.0"
        "androidx.fragment:fragment-ktx" version "1.1.0"

        group("androidx.lifecycle") {
            "lifecycle-extensions" version "2.1.0"
            "lifecycle-viewmodel-ktx" version "2.1.0"
            "lifecycle-livedata-core-ktx" version "2.1.0"
            "lifecycle-livedata-ktx" version "2.1.0"
        }

        group("org.jetbrains.kotlinx") {
            "kotlinx-coroutines-core" version "1.2.1"
            "kotlinx-coroutines-android" version "1.2.1"
        }

        "com.squareup.picasso:picasso" version "2.71828"

        "com.google.dagger:dagger" version "2.24"
    }
    kapt("com.google.dagger:dagger-compiler:2.24")

    // Testing libraries
    testImplementations {
        "junit:junit" version "4.12"
    }

    androidTestImplementations {
        "androidx.test:runner" version "1.1.1"
        "androidx.test.espresso:espresso-core" version "3.1.1"
    }
}
