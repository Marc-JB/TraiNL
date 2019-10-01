// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        infix fun String.version(version: String) = "${this}:$version"

        classpath("com.android.tools.build:gradle" version "3.5.0")
        classpath(kotlin("gradle-plugin", "1.3.50"))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    fun deleting(directory: File) = registering(Delete::class) { delete(directory) }

    val clean by deleting(rootProject.buildDir)
}