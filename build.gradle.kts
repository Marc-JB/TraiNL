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
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath(kotlin("gradle-plugin", version = Versions.Libs.kotlin))
        classpath(kotlin("serialization", version = Versions.Libs.kotlin))
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
            jvmVersion(Versions.SDK.jvm)

            kotlinOptions {
                useIR = true
                jvmTarget = Versions.SDK.jvm.toString()
            }
        }
    }
}

tasks {
    val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}
