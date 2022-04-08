package nl.marc_apps.ovgo.test_utils

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.koin.core.module.Module

inline fun withKoinModule(module: Module, block: (Application) -> Unit) {
    val app: KoinTestApplication = ApplicationProvider.getApplicationContext()
    app.withKoinModule(module) {
        block(app)
    }
}
