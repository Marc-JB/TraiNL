import org.gradle.api.JavaVersion

object Versions {
    val app = Version(0, 1)

    object SDK {
        val jvm = JavaVersion.VERSION_1_8

        object Android {
            const val compile = 30
            const val target = 30
            const val min = 21
        }
    }

    object Libs {
        const val kotlin = "1.4.10"
        const val koin = "2.0.1"

        object KotlinX {
            const val coroutines = "1.3.9"
            const val serialization = "1.0.0"
        }
    }
}
