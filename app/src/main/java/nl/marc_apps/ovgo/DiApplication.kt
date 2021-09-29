package nl.marc_apps.ovgo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@Suppress("unused")
class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DiApplication)
            modules(DiModules.dataRepositoriesModule, DiModules.viewModelsModule, DiModules.utilitiesModule)
        }
    }
}
