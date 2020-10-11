@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        val kotlinVersion = "1.4.10"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    tasks {
        withType<KotlinCompile>().configureEach {
            val jvmVersion = JavaVersion.VERSION_1_8.toString()
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion

            kotlinOptions {
                useIR = true
                jvmTarget = jvmVersion
            }
        }
    }
}

tasks {
    val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}
