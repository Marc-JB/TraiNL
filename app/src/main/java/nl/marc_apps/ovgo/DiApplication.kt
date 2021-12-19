package nl.marc_apps.ovgo

import android.app.Application
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
@ExperimentalSerializationApi
class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // TODO: Remove workaround for Kotlin 1.6. See https://github.com/InsertKoinIO/koin/issues/1188.
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@DiApplication)
            modules(DiModules.dataRepositoriesModule, DiModules.viewModelsModule, DiModules.utilitiesModule)
        }
    }
}
