@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(Versions.SDK.Android.compile)

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

        minSdkVersion(Versions.SDK.Android.min)
        targetSdkVersion(Versions.SDK.Android.target)

        versionCode = Versions.app.code
        versionName = Versions.app.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "NSR_KEYS_TRAVEL_API", project.findProperty("nsr.keys.travelApi") as String? ?: System.getenv("NSR_KEYS_TRAVEL_API"))
        buildConfigField("String", "NSR_KEYS_APP_API", project.findProperty("nsr.keys.travelApi") as String? ?: System.getenv("NSR_KEYS_APP_API"))
    }

    signingConfigs {
        val release by creating {
            storeFile = file("key.jks")
            storePassword = project.findProperty("android.store.password") as? String ?: System.getenv("ANDROID_STORE_PASSWORD")
            keyAlias = project.findProperty("android.key.alias") as? String ?: System.getenv("ANDROID_KEY_ALIAS")
            keyPassword = project.findProperty("android.key.password") as? String ?: System.getenv("ANDROID_KEY_PASSWORD")
        }
    }

    buildTypes {
        val release by getting {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            isCrunchPngs = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        val debug by getting {
            applicationIdSuffix = ".debug"

            isMinifyEnabled = false
            isZipAlignEnabled = true
            isShrinkResources = false
            isCrunchPngs = true
        }
    }

    flavorDimensions("api")

    productFlavors {
        val minApi21 by creating {
            minSdkVersion(21)
            versionCode = Versions.app flavor 0
        }

        val minApi26 by creating {
            minSdkVersion(26)
            versionCode = Versions.app flavor 1
        }
    }

    bundle {
        abi {
            enableSplit = true
        }

        language {
            enableSplit = true
        }

        density {
            enableSplit = true
        }

        texture {
            enableSplit = true
        }
    }

    compileOptions {
        sourceCompatibility = Versions.SDK.jvm
        targetCompatibility = Versions.SDK.jvm
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
    `kotlin-stdlib`

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")
}
