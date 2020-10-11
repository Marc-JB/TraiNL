import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.kotlin

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

val DependencyHandler.`kotlin-stdlib`
    get() = implementation(kotlin(if(Versions.SDK.jvm == JavaVersion.VERSION_1_8) "stdlib-jdk8" else "stdlib"))

val DependencyHandler.`kotlin-coroutines`
    get() = implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Libs.KotlinX.coroutines}")

val DependencyHandler.`kotlin-coroutines-android`
    get() = arrayOf(
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Libs.KotlinX.coroutines}"),
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Libs.KotlinX.coroutines}")
    )

val DependencyHandler.`kotlin-json-serialization`
    get() = arrayOf(
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.Libs.KotlinX.serialization}"),
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Libs.KotlinX.serialization}")
    )

fun AbstractCompile.jvmVersion(version: JavaVersion) {
    sourceCompatibility = version.toString()
    targetCompatibility = version.toString()
}